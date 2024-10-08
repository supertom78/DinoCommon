package fr.liksi.gitlab.llmagent.configuration.model;

public class GitlabProperties {

    private String ciDeployUser;

    private String ciDeployPassword;

    public String getCiDeployUser() {
        return ciDeployUser;
    }

    public GitlabProperties setCiDeployUser(String ciDeployUser) {
        this.ciDeployUser = ciDeployUser;
        return this;
    }

    public String getCiDeployPassword() {
        return ciDeployPassword;
    }

    public GitlabProperties setCiDeployPassword(String ciDeployPassword) {
        this.ciDeployPassword = ciDeployPassword;
        return this;
    }
}
