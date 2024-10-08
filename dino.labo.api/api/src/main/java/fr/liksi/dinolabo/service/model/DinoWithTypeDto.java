package fr.liksi.dinolabo.service.model;

import fr.liksi.dinolabo.configuration.headers.model.Parc;
import fr.liksi.dinolabo.repository.model.Status;
import fr.liksi.dinorepo.bean.DinoTypeRecordDto;

import java.util.UUID;

public record DinoWithTypeDto(UUID guid, String name, String species, String family, DinoTypeRecordDto.EraEnum era, Status status, Parc parc){
}
