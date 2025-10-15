package fr.liksi.dino.dto;

import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;

import java.util.UUID;

public record DinoWithTypeDto(UUID guid, String name, String species, String family, Era era, Status status,
                              Parc parc) {
}
