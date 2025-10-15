package fr.liksi.dino.service;

import fr.liksi.dino.DinoApi;
import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.DinoWithTypeDto;
import fr.liksi.dino.repository.DinoRepository;
import fr.liksi.dino.repository.model.Dino;
import fr.liksi.dinotype.DinotypeApi;
import fr.liksi.shared.config.headers.HeadersHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DinoService implements DinoApi {

    private static final Logger logger = LoggerFactory.getLogger(DinoService.class);

    private final DinoRepository repository;

    private final DinotypeApi dinotypeApi;

    private final HeadersHolder headersHolder;

    public DinoService(DinoRepository repository, DinotypeApi dinotypeApi, HeadersHolder headersHolder) {
        this.repository = repository;
        this.dinotypeApi = dinotypeApi;
        this.headersHolder = headersHolder;
    }

    public List<DinoWithTypeDto> getAllDinos() {
        logger.info("Calling getAllDinos");

        final var dinoTypes = dinotypeApi.getAllDinotypes();

        return repository.findAllByParc(this.headersHolder.getParc()).stream()
                .map(dino ->
                        dinoTypes.stream()
                                .filter(dinoType -> Objects.requireNonNull(dinoType.species()).equals(dino.species()))
                                .findFirst()
                                .map(dinotype -> new DinoWithTypeDto(dino.guid(), dino.name(), dino.species(), dinotype.family(), dinotype.era(), dino.status(), dino.parc()))
                                .orElse(new DinoWithTypeDto(dino.guid(), dino.name(), dino.species(), null, null, dino.status(), dino.parc()))
                )
                .toList();
    }

    public void addDino(DinoRecordDto dinoRecordDto) {
        logger.info("Calling addDino with {}", dinoRecordDto);
        repository.save(toDino(dinoRecordDto));
    }

    private Dino toDino(DinoRecordDto dinoRecordDto) {
        return new Dino()
                .guid(Objects.requireNonNullElse(dinoRecordDto.guid(), UUID.randomUUID()))
                .name(dinoRecordDto.name())
                .species(dinoRecordDto.species())
                .status(dinoRecordDto.status())
                .parc(headersHolder.getParc());
    }
}
