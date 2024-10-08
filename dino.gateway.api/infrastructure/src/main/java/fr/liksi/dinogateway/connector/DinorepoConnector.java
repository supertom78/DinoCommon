package fr.liksi.dinogateway.connector;

import fr.liksi.dinogateway.DinoType;
import fr.liksi.dinogateway.spi.DinoTypeInventory;
import fr.liksi.dinorepo.api.DinoControllerApi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DinorepoConnector implements DinoTypeInventory {

    private final DinoControllerApi dinorepoControllerApi;

    public DinorepoConnector(DinoControllerApi dinorepoControllerApi) {
        this.dinorepoControllerApi = dinorepoControllerApi;
    }

    @Override
    public List<DinoType> getAll() {
        return dinorepoControllerApi.getAllDinoTypes().stream()
                .map(dinoTypeRecordDto -> new DinoType(
                        dinoTypeRecordDto.getSpecies(),
                        dinoTypeRecordDto.getFamily())
                ).toList();
    }
}
