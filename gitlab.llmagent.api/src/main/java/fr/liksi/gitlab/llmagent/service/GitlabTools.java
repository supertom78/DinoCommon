package fr.liksi.gitlab.llmagent.service;

import dev.langchain4j.agent.tool.Tool;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class GitlabTools {

    private final static Logger LOG = LoggerFactory.getLogger(GitlabTools.class);

    private Map<RevCommit, String> currentCommitDiffs;

    public GitlabTools() {}

    public GitlabTools setCurrentCommitDiffs(Map<RevCommit, String> currentCommitDiffs) {
        this.currentCommitDiffs = currentCommitDiffs;
        return this;
    }

    @Tool
    String getFileContent(String absolutePathFile) throws IOException {
        LOG.info("TOOLS - Reading file content: {}", absolutePathFile);
        return FileUtils.readFileToString(new File(absolutePathFile), "UTF-8");
    }

    @Tool
    String getCommitDiffs(String commitName) {
        LOG.info("TOOLS - Getting commit diff from : {}", commitName);
        final var matchingKey = currentCommitDiffs.keySet().stream()
                .filter(revCommit -> revCommit.getName().equals(commitName))
                .findFirst()
                .orElseThrow();
        return currentCommitDiffs.get(matchingKey);
    }
}
