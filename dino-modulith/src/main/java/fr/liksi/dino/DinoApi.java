package fr.liksi.dino;

import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.DinoWithTypeDto;

import java.util.List;

public interface DinoApi {

    /**
     * Retrieves all dinosaurs with their associated type information
     *
     * @return List of dinosaurs with type details
     */
    List<DinoWithTypeDto> getAllDinos();

    /**
     * Default method to add a new dinosaur
     *
     * @param dinoRecordDto The dinosaur record to add
     */
    default void addDino(DinoRecordDto dinoRecordDto) {
        // Default implementation does nothing
    }
}
