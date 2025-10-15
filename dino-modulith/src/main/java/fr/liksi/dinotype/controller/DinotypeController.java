package fr.liksi.dinotype.controller;

import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.dinotype.service.DinoRepoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DinotypeController {

    private static final Logger logger = LoggerFactory.getLogger(DinotypeController.class);

    private final DinoRepoService dinoRepoService;

    public DinotypeController(DinoRepoService dinoRepoService) {
        this.dinoRepoService = dinoRepoService;
    }

    @GetMapping("/dinotypes")
    @Secured({"ROLE_PALEO", "ROLE_MARKETING", "ROLE_LABO", "ROLE_MANAGER", "ROLE_ADMIN"})
    public List<DinotypeRecordDto> getAllDinotypes() {
        logger.info("Calling GET /dinotypes");
        return dinoRepoService.getAllDinotypes();
    }

    @PostMapping("/dinotype")
    @Secured({"ROLE_PALEO", "ROLE_MARKETING", "ROLE_ADMIN"})
    public ResponseEntity<Void> addDinotype(@Valid @RequestBody DinotypeRecordDto dinoTypeRecordDto) {
        logger.info("Calling POST /dinotype");
        dinoRepoService.addDinotype(dinoTypeRecordDto);
        return ResponseEntity.ok().build();
    }
}
