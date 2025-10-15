package fr.liksi.dino.dto;


import fr.liksi.shared.kernel.Parc;

import java.util.UUID;

public record DinoRecordDto(UUID guid, String name, String species, Status status, Parc parc) {
}
