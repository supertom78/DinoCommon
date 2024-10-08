package fr.liksi.dinogateway.connector;

import fr.liksi.dinogateway.Dino;
import fr.liksi.dinogateway.DinoStatusEnum;
import fr.liksi.dinogateway.spi.DinoInventory;
import fr.liksi.dinolabo.api.DinoControllerApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DinolaboConnector implements DinoInventory {

    private final DinoControllerApi dinolaboControllerApi;

    public DinolaboConnector(DinoControllerApi dinolaboControllerApi) {
        this.dinolaboControllerApi = dinolaboControllerApi;
    }

    @Override
    public List<Dino> getAll() {
        return dinolaboControllerApi.getAllDinos().stream()
                .map(dinoWithTypeDto -> new Dino(
                        dinoWithTypeDto.getName(),
                        dinoWithTypeDto.getSpecies(),
                        Optional.ofNullable(dinoWithTypeDto.getStatus()).map(Enum::name).map(DinoStatusEnum::valueOf).orElse(DinoStatusEnum.UNKNOWN))
                ).toList();
    }
}
