package fr.liksi.dinolabo.connector;

import fr.liksi.dinorepo.api.DinoControllerApi;
import fr.liksi.dinorepo.bean.DinoTypeRecordDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DinorepoConnector {

    private final DinoControllerApi dinorepoControllerApi;

    public DinorepoConnector(DinoControllerApi dinorepoControllerApi) {
        this.dinorepoControllerApi = dinorepoControllerApi;
    }

    public List<DinoTypeRecordDto> getAllDinotypes() {
        return dinorepoControllerApi.getAllDinoTypes();
    }
}
