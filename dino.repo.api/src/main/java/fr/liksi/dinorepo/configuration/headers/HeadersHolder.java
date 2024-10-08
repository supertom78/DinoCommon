package fr.liksi.dinorepo.configuration.headers;

import fr.liksi.dinorepo.configuration.headers.model.Parc;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class HeadersHolder {

    private Parc parc;

    private UUID correlationId;

    private static final Logger LOG = LoggerFactory.getLogger(HeadersHolder.class);

    public Parc getParc() {
        return parc;
    }

    public void setHeadersFromRequest(HttpServletRequest request){
        parc = HeaderUtils.getParcFromRequest(request).orElse(null);
        correlationId = request.getHeader("correlationId") != null ? UUID.fromString(request.getHeader("correlationId")) : UUID.randomUUID();
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void clear() {
        LOG.info("Clear parc header");
        parc = null;
    }
}
