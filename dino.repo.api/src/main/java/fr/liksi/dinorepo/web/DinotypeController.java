package fr.liksi.dinorepo.web;

import fr.liksi.dinorepo.repository.model.DinotypeRecordDto;
import fr.liksi.dinorepo.service.DinoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DinotypeController {

    private static final Logger logger = LoggerFactory.getLogger(DinotypeController.class);

    private final DinoService dinoService;

    public DinotypeController(DinoService dinoService) {
        this.dinoService = dinoService;
    }

    @GetMapping("/dinotypes")
    @Secured({"ROLE_PALEO", "ROLE_MARKETING", "ROLE_LABO", "ROLE_MANAGER", "ROLE_ADMIN"})
    public List<DinotypeRecordDto> getAllDinotypes() {
        logger.info("Calling GET /dinotypes");
        return dinoService.getAllDinotypes();
    }

    @PostMapping("/dinotype")
    @Secured({"ROLE_PALEO", "ROLE_MARKETING", "ROLE_ADMIN"})
    public void addDinotype(@Valid @RequestBody DinotypeRecordDto dinoTypeRecordDto) {
        logger.info("Calling POST /dinotype");
        dinoService.addDinotype(dinoTypeRecordDto);
    }
}
