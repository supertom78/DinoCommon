package fr.liksi.dino.web;

import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.DinoWithTypeDto;
import fr.liksi.dino.service.DinoService;
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
    @Secured({"ROLE_LABO", "ROLE_MANAGER"})
    public List<DinoWithTypeDto> getAllDinos() {
        logger.info("Calling GET /dinos");
        return dinoService.getAllDinos();
    }

    @PostMapping("/dino")
    @Secured({"ROLE_LABO", "ROLE_MANAGER", "ROLE_ADMIN"})
    public void addDino(@Valid @RequestBody DinoRecordDto dinoRecordDto) {
        logger.info("Calling POST /dino");
        dinoService.addDino(dinoRecordDto);
    }
}
