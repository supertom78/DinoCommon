package fr.liksi.dinogateway.configuration.ext.model;

public class ApiProperties {
    private String scheme;
    private String host;
    private String port;

    public String getScheme() {
        return scheme;
    }

    public ApiProperties setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ApiProperties setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public ApiProperties setPort(String port) {
        this.port = port;
        return this;
    }
}
