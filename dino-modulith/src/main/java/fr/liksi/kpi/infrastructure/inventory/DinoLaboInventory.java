package fr.liksi.kpi.infrastructure.inventory;

import fr.liksi.kpi.domain.spi.DinoInventory;
import fr.liksi.kpi.domain.spi.model.Dino;
import fr.liksi.kpi.domain.spi.model.DinoStatusEnum;
import fr.liksi.dino.DinoApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DinoLaboInventory implements DinoInventory {

    private final DinoApi dinoApi;

    public DinoLaboInventory(DinoApi dinoApi) {
        this.dinoApi = dinoApi;
    }

    @Override
    public List<Dino> getAll() {
        return dinoApi.getAllDinos().stream()
                .map(dinoWithTypeDto -> new Dino(
                        dinoWithTypeDto.name(),
                        dinoWithTypeDto.species(),
                        Optional.ofNullable(dinoWithTypeDto.status()).map(Enum::name).map(DinoStatusEnum::valueOf).orElse(DinoStatusEnum.UNKNOWN))
                ).toList();
    }
}
