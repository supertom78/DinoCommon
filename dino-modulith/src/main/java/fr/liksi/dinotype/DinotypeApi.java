package fr.liksi.dinotype;

import fr.liksi.dinotype.dto.DinotypeRecordDto;

import java.util.List;

public interface DinotypeApi {

    /**
     * Retrieves all dinosaur types for the current park
     *
     * @return List of dinosaur type DTOs
     */
    List<DinotypeRecordDto> getAllDinotypes();

    /**
     * Adds a new dinosaur type
     *
     * @param dinotypeRecordDto The dinosaur type data
     */
    default void addDinotype(DinotypeRecordDto dinotypeRecordDto) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
