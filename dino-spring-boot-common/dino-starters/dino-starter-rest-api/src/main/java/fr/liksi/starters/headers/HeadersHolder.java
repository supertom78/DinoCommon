package fr.liksi.starters.headers;

import fr.liksi.utils.model.Parc;
import fr.liksi.utils.HeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class HeadersHolder {

    private Parc parc;

    private UUID correlationId;

    private String authorization;

    private final boolean createCorrelationId;

    private static final Logger LOG = LoggerFactory.getLogger(HeadersHolder.class);

    public HeadersHolder(HeadersHolderConfig headersHolderConfig) {
        this.createCorrelationId = headersHolderConfig.isCreateCorrelationId();
    }

    public Parc getParc() {
        return parc;
    }

    public void setHeadersFromRequest(HttpServletRequest request){
        parc = HeaderUtils.getParcFromRequest(request).orElse(null);
        correlationId = Optional.ofNullable(request.getHeader("correlationId"))
                        .map(UUID::fromString)
                        .orElse(createCorrelationId ? UUID.randomUUID() : null);
        authorization = request.getHeader("Authorization");
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void clear() {
        LOG.info("Clear parc header");
        parc = null;
    }
}
