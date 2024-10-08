package fr.liksi.gitlab.llmagent.service;

import fr.liksi.gitlab.llmagent.configuration.model.GitlabProperties;
import fr.liksi.gitlab.llmagent.web.model.GitlabCommitPropagationRequest;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MockitoSettings
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitlabServiceTest {

    private GitlabService gitlabService;

    @BeforeAll
    void setUp() throws IOException {
        this.gitlabService = new GitlabService(new GitlabProperties()
                .setCiDeployUser(System.getenv("GITLAB_CI_DEPLOY_USER"))
                .setCiDeployPassword(System.getenv("GITLAB_CI_DEPLOY_PASSWORD")));
    }

    @Test
    void should_prepareGitCommitsDataToAnalyseOnDinoLaboRepo() throws GitAPIException, IOException {
        final var gitlabCommitRequest = new GitlabCommitPropagationRequest(
                "https://gitlab.liksi.io/liksi/interne/tremplincommon/dino.labo.api.git",
                "develop",
                "https://gitlab.liksi.io/liksi/interne/tremplincommon/dino.repo.api.git",
                "develop",
                "test/test1",
                "ac8d0d9e7c1e66eae3cc0232dc8be1b4e24a140f");

        final var gitCommitDatasToAnalyse = gitlabService.prepareGitCommitsDataToAnalyse(gitlabCommitRequest);
        assertThat(gitCommitDatasToAnalyse).isNotNull();
        assertThat(gitCommitDatasToAnalyse.targetedRepoDirFile()).isNotNull();
        assertThat(gitCommitDatasToAnalyse.commonCommitDiffs().keySet().size()).isEqualTo(3);
    }


}