package fr.liksi.kpi.infrastructure.inventory;

import fr.liksi.kpi.domain.spi.DinoTypeInventory;
import fr.liksi.kpi.domain.spi.model.DinoType;
import fr.liksi.dinotype.DinotypeApi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DinoRepoInventory implements DinoTypeInventory {

    private final DinotypeApi dinotypeApi;

    public DinoRepoInventory(DinotypeApi dinotypeApi) {
        this.dinotypeApi = dinotypeApi;
    }

    @Override
    public List<DinoType> getAll() {
        return dinotypeApi.getAllDinotypes().stream()
                .map(dinoTypeRecordDto -> new DinoType(
                        dinoTypeRecordDto.species(),
                        dinoTypeRecordDto.family())
                ).toList();
    }
}
