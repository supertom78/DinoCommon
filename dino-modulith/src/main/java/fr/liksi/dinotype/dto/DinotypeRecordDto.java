package fr.liksi.dinotype.dto;


import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;

import java.util.UUID;

public record DinotypeRecordDto(UUID guid, String species, String family, Era era, Parc parc) {
}
