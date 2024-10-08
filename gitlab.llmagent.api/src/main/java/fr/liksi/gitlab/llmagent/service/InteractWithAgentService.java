package fr.liksi.gitlab.llmagent.service;

import fr.liksi.gitlab.llmagent.service.model.GitCommitDatasToAnalyse;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class InteractWithAgentService {

    private final static Logger LOG = LoggerFactory.getLogger(InteractWithAgentService.class);

    private final GitlabLLMAgent gitlabLLMAgent;

    private final GitlabService gitlabService;

    private final GitlabTools gitlabTools;

    private final LLMResultParser llmResultParser;

    public InteractWithAgentService(GitlabLLMAgent gitlabLLMAgent, GitlabService gitlabService, GitlabTools gitlabTools, LLMResultParser llmResultParser) {
        this.gitlabLLMAgent = gitlabLLMAgent;
        this.gitlabService = gitlabService;
        this.gitlabTools = gitlabTools;
        this.llmResultParser = llmResultParser;
    }

    public void interactWithAgent(GitCommitDatasToAnalyse gitCommitDatasToAnalyse){
        LOG.info("Start interaction with agent");

        this.gitlabTools.setCurrentCommitDiffs(gitCommitDatasToAnalyse.commonCommitDiffs());
        gitCommitDatasToAnalyse.getCommonCommitsOrderByAscendingDate()
                .forEach(revCommit -> analyseAndPropagateCommitOnAllFiles(gitCommitDatasToAnalyse, revCommit));

    }

    private void analyseAndPropagateCommitOnAllFiles(GitCommitDatasToAnalyse gitCommitDatasToAnalyse, RevCommit revCommit) {
        LOG.info("Analyse and propagate commit: " + revCommit.getFullMessage());
        gitCommitDatasToAnalyse.getAllFilesOnTargetedRepo().forEach(file -> {
            final var userPrompt = "Analyse et propage les diffs git du commit "+revCommit.getName()+" sur le fichier " + file.getAbsolutePath();
            LOG.info("User prompt: {}", userPrompt);
            final var updatedFileContent = gitlabLLMAgent.analyseAndPropagateCommit(userPrompt);

            LOG.info("Sleeping for 20 seconds to avoid TPM limit");
            sleep(20000);
            LOG.debug("LLM response to User prompt {} = {}", userPrompt, updatedFileContent);
            llmResultParser.getParsedResult(updatedFileContent)
                    .ifPresentOrElse(
                            parsedResult -> overwriteFileContentIfNeeded(file, parsedResult),
                            () -> LOG.info("No content to overwrite for file: {} - {}", file.getAbsolutePath(),updatedFileContent));
        });
        gitlabService.commitAllFiles(gitCommitDatasToAnalyse.targetedRepoDirFile(), revCommit.getFullMessage());
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOG.debug("Error while sleeping", e);
        }
    }


    private void overwriteFileContentIfNeeded(File file, String updatedFileContent) {
        try {
            LOG.info("Overwriting file content: {} with {}", file.getAbsolutePath(), updatedFileContent);
            FileUtils.writeStringToFile(file, updatedFileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Error while overwriting file content", e);
        }
    }


}
