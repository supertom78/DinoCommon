package fr.liksi.starters.headers;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("headersholder")
public class HeadersHolderConfig {

    /**
     * Create a correlation id if not present in the request.
     */
    private boolean createCorrelationId  = true;

    public boolean isCreateCorrelationId() {
        return createCorrelationId;
    }

    public HeadersHolderConfig setCreateCorrelationId(boolean createCorrelationId) {
        this.createCorrelationId = createCorrelationId;
        return this;
    }
}
