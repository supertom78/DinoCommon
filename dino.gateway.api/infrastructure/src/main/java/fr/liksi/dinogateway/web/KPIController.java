package fr.liksi.dinogateway.web;

import fr.liksi.dinogateway.Kpis;
import fr.liksi.dinogateway.api.CreateKpis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KPIController {

    private static final Logger logger = LoggerFactory.getLogger(KPIController.class);

    private final CreateKpis createKpis;

    public KPIController(CreateKpis createKpis) {
        this.createKpis = createKpis;
    }


    @GetMapping("/kpis")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public Kpis getAllKpis() {
        logger.info("Calling GET /kpis");
        return createKpis.consolidate();
    }
}
