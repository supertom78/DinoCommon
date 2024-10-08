package fr.liksi.gitlab.llmagent.service;

import fr.liksi.gitlab.llmagent.configuration.model.GitlabProperties;
import fr.liksi.gitlab.llmagent.service.model.GitCommitDatasToAnalyse;
import fr.liksi.gitlab.llmagent.web.model.GitlabCommitPropagationRequest;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class GitlabService {

    private final static Logger LOG = LoggerFactory.getLogger(GitlabService.class);

    private final GitlabProperties gitlabProperties;

    private final File tempDir;

    public GitlabService(GitlabProperties gitlabProperties) throws IOException {
        this.gitlabProperties = gitlabProperties;
        tempDir = Files.createTempDirectory(Path.of("/tmp/gitlab"),"gitlabllmagent").toFile();
        FileUtils.deleteDirectory(tempDir);
    }

    public GitCommitDatasToAnalyse prepareGitCommitsDataToAnalyse(GitlabCommitPropagationRequest gitlabCommitPropagationRequest) throws GitAPIException, IOException {
        return new GitCommitDatasToAnalyse(
                cloneRepoToImpactAndCheckoutBranchToCreate(gitlabCommitPropagationRequest),
                cloneCommonRepoAndComputeDiffsCommits(gitlabCommitPropagationRequest));
    }

    public void commitAllFiles(File repoDir, String commitMessage) {
        try (Git git = Git.open(repoDir)) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage(commitMessage).call();
        } catch (IOException | GitAPIException e) {
            LOG.error("Error while committing all files", e);
        }
    }

    private File cloneRepoToImpactAndCheckoutBranchToCreate(GitlabCommitPropagationRequest gitlabCommitPropagationRequest) throws GitAPIException, IOException {
        final var targetRepoDir = Files.createDirectories(Path.of(tempDir.getAbsolutePath()+"/target")).toFile();
        FileUtils.deleteDirectory(targetRepoDir);
        cloneGitRepo(gitlabCommitPropagationRequest.repoToImpact(), gitlabCommitPropagationRequest.branchToImpact(), targetRepoDir);
        checkoutNewBranch(targetRepoDir, gitlabCommitPropagationRequest.branchToCreate());
        return targetRepoDir;
    }

    private Map<RevCommit, String> cloneCommonRepoAndComputeDiffsCommits(GitlabCommitPropagationRequest gitlabCommitPropagationRequest) throws GitAPIException, IOException {
        final var sourceRepoDir = Files.createDirectories(Path.of(tempDir.getAbsolutePath()+"/source")).toFile();
        FileUtils.deleteDirectory(sourceRepoDir);
        final var gitSourceRepo = cloneGitRepo(gitlabCommitPropagationRequest.repoSource(), gitlabCommitPropagationRequest.brancheSource(), sourceRepoDir);
        Map<RevCommit, String> mapCommitDiffs = getCommitsDiffs(gitlabCommitPropagationRequest, gitSourceRepo);
        LOG.info("Diffs between commits to analyse : {}", mapCommitDiffs);
        return mapCommitDiffs;
    }

    @NotNull
    private Map<RevCommit, String> getCommitsDiffs(GitlabCommitPropagationRequest gitlabCommitPropagationRequest, Git gitSourceRepo) throws IOException {
        RevWalk walk = new RevWalk(gitSourceRepo.getRepository());
        RevCommit targetCommit = walk.parseCommit(ObjectId.fromString(gitlabCommitPropagationRequest.commitSHA()));
        RevCommit lastCommit = walk.parseCommit(targetCommit.getParent(0));

        RevCommit currentCommit = walk.parseCommit(targetCommit.getParents().length > 1 ? targetCommit.getParent(1) : lastCommit);

        Map<RevCommit,String> mapCommitDiffs = new HashMap<>();
        boolean stop = currentCommit == null;
        while(!stop){
            final var commitDiff = getCommitDiffs(gitSourceRepo.getRepository(), currentCommit, targetCommit);
            if(StringUtils.hasLength(commitDiff)){
                mapCommitDiffs.put(targetCommit,commitDiff);
            }

            targetCommit =  walk.parseCommit(currentCommit.getId());
            currentCommit = walk.parseCommit(currentCommit.getParent(0));
            stop = targetCommit == null || targetCommit.getId().equals(lastCommit.getId());

        }
        return mapCommitDiffs;
    }

    private String getCommitDiffs(Repository repository, RevCommit previousCommit, RevCommit commit) throws IOException {
        LOG.debug("Get diff between commit {}-{} and commit {}-{}", previousCommit.getFullMessage(), previousCommit.getName(), commit.getFullMessage(), commit.getName());
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        try (DiffFormatter diffFormatter = new DiffFormatter(stdout)) {
            diffFormatter.setRepository(repository);
            for (DiffEntry entry : diffFormatter.scan(previousCommit, commit)) {
                diffFormatter.format(diffFormatter.toFileHeader(entry));
            }
        }
        return stdout.toString();
    }

    private Git cloneGitRepo(String repoUrl, String brancheName, File repoDir) throws GitAPIException {
        LOG.info("Cloning repo {} using branch {} on {}",repoUrl, brancheName, repoDir.getAbsolutePath());
        try {
            return Git.cloneRepository()
                    .setCredentialsProvider(getCredentialsProvider())
                    .setURI(repoUrl)
                    .setDirectory(repoDir)
                    .setBranch(brancheName)
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while cloning repo", e);
            throw e;
        }
    }

    @NotNull
    private UsernamePasswordCredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(gitlabProperties.getCiDeployUser(), gitlabProperties.getCiDeployPassword());
    }

    private void checkoutNewBranch(File repoDir, String branchName){
        try (Git git = Git.open(repoDir)) {
            git.checkout()
                    .setName(branchName)
                    .setCreateBranch(true)
                    .call();
        } catch (IOException | GitAPIException e) {
            LOG.error("Error while branchCreate", e);
        }
    }


    public void pushToGitlab(File repoDir, String branchName) {
        try (Git git = Git.open(repoDir)) {
            git.push()
                    .setCredentialsProvider(getCredentialsProvider())
                    .setRemote("origin")
                    .setRefSpecs(new RefSpec(branchName+":"+branchName))
                    .call();
        } catch (IOException | GitAPIException e) {
            LOG.error("Error while pushing", e);
        }

    }
}
