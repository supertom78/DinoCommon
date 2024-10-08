package fr.liksi.dinolabo.web;

import fr.liksi.dinolabo.service.DinoService;
import fr.liksi.dinolabo.repository.model.DinoRecordDto;
import fr.liksi.dinolabo.service.model.DinoWithTypeDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DinoController {

    private static final Logger logger = LoggerFactory.getLogger(DinoController.class);

    private final DinoService dinoService;

    public DinoController(DinoService dinoService) {
        this.dinoService = dinoService;
    }

    @GetMapping("/dinos")
    @Secured({"ROLE_LABO", "ROLE_MANAGER", "ROLE_ADMIN"})
    public List<DinoWithTypeDto> getAllDinos() {
        logger.info("Calling GET /dinos");
        return dinoService.getAllDinos();
    }

    @PostMapping("/dino")
    @Secured({"ROLE_LABO", "ROLE_ADMIN"})
    public void addDino(@Valid @RequestBody DinoRecordDto dinoRecordDto) {
        logger.info("Calling POST /dino");
        dinoService.addDino(dinoRecordDto);
    }
}
