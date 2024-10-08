package fr.liksi.dinogateway.configuration.headers;

import fr.liksi.dinogateway.configuration.headers.model.Parc;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class HeaderUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderUtils.class);

    public static Optional<Parc> getParcFromRequest(HttpServletRequest request){
        String parcHeader = request.getHeader("parc");
        LOG.info("Getting parc header from request {}", parcHeader);
        if(parcHeader != null){
            try {
                return Optional.of(Parc.valueOf(parcHeader));
            } catch (IllegalArgumentException e){
                LOG.error("Invalid parc header value : {}", parcHeader);
            }
        }else {
            LOG.error("Parc header is missing");
        }
        return Optional.empty();
    }
}
