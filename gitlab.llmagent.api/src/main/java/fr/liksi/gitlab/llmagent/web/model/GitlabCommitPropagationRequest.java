package fr.liksi.gitlab.llmagent.web.model;

public record GitlabCommitPropagationRequest(String repoSource, String brancheSource, String repoToImpact, String branchToImpact, String branchToCreate, String commitSHA) {
}
