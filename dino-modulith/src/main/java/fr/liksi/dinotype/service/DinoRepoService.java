package fr.liksi.dinotype.service;

import fr.liksi.dinotype.DinotypeApi;
import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.dinotype.repository.DinotypeRepository;
import fr.liksi.dinotype.repository.model.Dinotype;
import fr.liksi.shared.config.headers.HeadersHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DinoRepoService implements DinotypeApi {

    private static final Logger logger = LoggerFactory.getLogger(DinoRepoService.class);

    private final DinotypeRepository repository;

    private final HeadersHolder headersHolder;

    public DinoRepoService(DinotypeRepository repository, HeadersHolder headersHolder) {
        this.repository = repository;
        this.headersHolder = headersHolder;
    }

    public List<DinotypeRecordDto> getAllDinotypes() {
        logger.info("Calling getAllDinotypes");
        return repository.findAllByParc(this.headersHolder.getParc()).stream()
                .map(dinotype -> new DinotypeRecordDto(
                        dinotype.getGuid(),
                        dinotype.getSpecies(),
                        dinotype.getFamily(),
                        dinotype.getEra(),
                        dinotype.getParc()))
                .toList();
    }

    public void addDinotype(DinotypeRecordDto dinotypeRecordDto) {
        logger.info("Calling addDinotype with {}", dinotypeRecordDto);
        repository.save(new Dinotype()
                .guid(Objects.requireNonNullElse(dinotypeRecordDto.guid(), UUID.randomUUID()))
                .species(dinotypeRecordDto.species())
                .family(dinotypeRecordDto.family())
                .era(dinotypeRecordDto.era())
                .parc(headersHolder.getParc()));
    }
}
