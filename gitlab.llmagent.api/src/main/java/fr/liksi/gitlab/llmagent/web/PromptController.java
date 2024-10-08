package fr.liksi.gitlab.llmagent.web;

import fr.liksi.gitlab.llmagent.service.GitlabService;
import fr.liksi.gitlab.llmagent.service.InteractWithAgentService;
import fr.liksi.gitlab.llmagent.web.model.GitlabCommitPropagationRequest;
import jakarta.validation.Valid;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PromptController {

    private static final Logger logger = LoggerFactory.getLogger(PromptController.class);

    private final GitlabService gitlabService;

    private final InteractWithAgentService interactWithAgentService;


    public PromptController(GitlabService gitlabService, InteractWithAgentService interactWithAgentService) {

        this.gitlabService = gitlabService;
        this.interactWithAgentService = interactWithAgentService;
    }

    @PostMapping("/propagate")
    public void propagate(@Valid @RequestBody GitlabCommitPropagationRequest gitlabCommitPropagationRequest) throws GitAPIException, IOException {
        logger.info("Calling POST /commitRequest");

        final var gitCommitDatasToAnalyse = gitlabService.prepareGitCommitsDataToAnalyse(gitlabCommitPropagationRequest);
        interactWithAgentService.interactWithAgent(gitCommitDatasToAnalyse);
        //gitlabService.pushToGitlab(gitCommitDatasToAnalyse.targetedRepoDirFile(), gitlabCommitPropagationRequest.branchToCreate());

    }
}
