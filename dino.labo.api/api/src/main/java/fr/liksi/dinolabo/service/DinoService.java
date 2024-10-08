package fr.liksi.dinolabo.service;

import fr.liksi.dinolabo.connector.DinorepoConnector;
import fr.liksi.dinolabo.repository.DinoRepository;
import fr.liksi.dinolabo.repository.model.Dino;
import fr.liksi.dinolabo.repository.model.DinoRecordDto;
import fr.liksi.dinolabo.service.model.DinoWithTypeDto;
import fr.liksi.starters.headers.HeadersHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DinoService {

    private static final Logger logger = LoggerFactory.getLogger(DinoService.class);

    private final DinoRepository repository;

    private final DinorepoConnector dinorepoConnector;

    private final HeadersHolder headersHolder;

    public DinoService(DinoRepository repository, DinorepoConnector dinorepoConnector, HeadersHolder headersHolder) {
        this.repository = repository;
        this.dinorepoConnector = dinorepoConnector;
        this.headersHolder = headersHolder;
    }

    public List<DinoWithTypeDto> getAllDinos(){
        logger.info("Calling getAllDinos");

        final var dinoTypes = dinorepoConnector.getAllDinotypes();

        return repository.findAllByParc(this.headersHolder.getParc()).stream()
                .map(dino ->
                     dinoTypes.stream()
                            .filter(dinoType -> Objects.requireNonNull(dinoType.getSpecies()).equals(dino.species()))
                            .findFirst()
                            .map(dinotype -> new DinoWithTypeDto(dino.guid(), dino.name(), dino.species(), dinotype.getFamily(), dinotype.getEra(), dino.status(), dino.parc()))
                            .orElse(new DinoWithTypeDto(dino.guid(), dino.name(), dino.species(), null, null, dino.status(), dino.parc()))
                )
                .toList();
    }

    public void addDino(DinoRecordDto dinoRecordDto) {
        logger.info("Calling addDino with {}", dinoRecordDto);
        repository.save(new Dino()
                .guid(Objects.requireNonNullElse(dinoRecordDto.guid(),UUID.randomUUID()))
                .name(dinoRecordDto.name())
                .species(dinoRecordDto.species())
                .status(dinoRecordDto.status())
                .parc(headersHolder.getParc()));
    }
}
