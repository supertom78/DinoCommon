package fr.liksi.dinorepo.service;

import fr.liksi.dinorepo.repository.DinotypeRepository;
import fr.liksi.dinorepo.repository.model.Dinotype;
import fr.liksi.dinorepo.repository.model.DinotypeRecordDto;
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

    private final DinotypeRepository repository;

    private final HeadersHolder headersHolder;

    public DinoService(DinotypeRepository repository, HeadersHolder headersHolder) {
        this.repository = repository;
        this.headersHolder = headersHolder;
    }

    public List<DinotypeRecordDto> getAllDinotypes(){
        logger.info("Calling getAllDinotypes");
        return repository.findAllByParc(this.headersHolder.getParc()).stream()
                .toList();
    }

    public void addDinotype(DinotypeRecordDto dinotypeRecordDto) {
        logger.info("Calling addDinotype with {}", dinotypeRecordDto);
        repository.save(new Dinotype()
                .guid(Objects.requireNonNullElse(dinotypeRecordDto.guid(),UUID.randomUUID()))
                .species(dinotypeRecordDto.species())
                .family(dinotypeRecordDto.family())
                .era(dinotypeRecordDto.era())
                .parc(headersHolder.getParc()));
    }
}
