package fr.liksi.dinolabo.repository.model;

import fr.liksi.dinolabo.configuration.headers.model.Parc;

import java.util.UUID;

public record DinoRecordDto(UUID guid, String name, String species, Status status, Parc parc){
}
