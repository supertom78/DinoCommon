package fr.liksi.kpi.infrastructure.web;

import fr.liksi.kpi.dto.Kpis;
import fr.liksi.kpi.infrastructure.service.KPIService;
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

    private final KPIService kpiService;

    public KPIController(KPIService kpiService) {
        this.kpiService = kpiService;
    }


    @GetMapping("/kpis")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public Kpis getAllKpis() {
        logger.info("Calling GET /kpis");
        return kpiService.getAllKpis();
    }
}
