package fr.liksi.dinorepo.repository.model;

import fr.liksi.dinorepo.configuration.headers.model.Parc;

import java.util.UUID;

public record DinotypeRecordDto(UUID guid, String species, String family, Era era, Parc parc){
}
