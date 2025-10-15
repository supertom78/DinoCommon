package fr.liksi.kpi.stub;

import fr.liksi.dino.DinoApi;
import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.DinoWithTypeDto;
import fr.liksi.dino.dto.Status;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestComponent
public class DinoApiStub implements DinoApi {

    private List<DinoWithTypeDto> dinos = new ArrayList<>();

    public void setTestData(List<DinoWithTypeDto> testDinos) {
        this.dinos = new ArrayList<>(testDinos);
    }

    public void clearData() {
        this.dinos.clear();
    }

    @Override
    public List<DinoWithTypeDto> getAllDinos() {
        return new ArrayList<>(dinos);
    }

    @Override
    public void addDino(DinoRecordDto dinoRecordDto) {
        // Générer un GUID si null
        UUID guid = dinoRecordDto.guid() != null ? dinoRecordDto.guid() : UUID.randomUUID();

        // Convertir DinoRecordDto vers DinoWithTypeDto avec des données par défaut pour family et era
        DinoWithTypeDto dinoWithType = new DinoWithTypeDto(
            guid,
            dinoRecordDto.name(),
            dinoRecordDto.species(),
            getDefaultFamily(dinoRecordDto.species()), // Méthode helper pour déterminer la famille
            getDefaultEra(dinoRecordDto.species()),     // Méthode helper pour déterminer l'ère
            dinoRecordDto.status(),
            dinoRecordDto.parc()
        );

        dinos.add(dinoWithType);
    }

    // Méthodes utilitaires pour les tests KPI
    public void addTestDinosForBasicKpis() {
        clearData();
        // Pour avoir : nbDinoToCreate=2, nbDinoInCreation=1, nbDinoCreated=2
        // Avec 3 dinotypes et 2 dinos créés -> nbDinoToCreate = 3-2 = 1 (pas 2!)
        // Je dois ajuster : avec 4 dinotypes et 2 dinos créés -> nbDinoToCreate = 4-2 = 2
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Rex1", "T-Rex", "Tyrannosauridae", Era.CRETACEOUS, Status.ADN, Parc.HAWAII));        // IN_CREATION
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Velo1", "Velociraptor", "Dromaeosauridae", Era.CRETACEOUS, Status.ADULT, Parc.HAWAII));  // CREATED
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Velo2", "Velociraptor", "Dromaeosauridae", Era.CRETACEOUS, Status.BABY, Parc.HAWAII));   // CREATED
    }

    public void addTestDinosForMultipleStatuses() {
        clearData();
        // Pour avoir : nbDinoToCreate=3, nbDinoInCreation=2, nbDinoCreated=4
        // Avec X dinotypes et 4 dinos créés -> nbDinoToCreate = X-4 = 3 -> X = 7 dinotypes nécessaires
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Rex3", "T-Rex", "Tyrannosauridae", Era.CRETACEOUS, Status.ADN, Parc.HAWAII));         // IN_CREATION
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Trike2", "Triceratops", "Ceratopsidae", Era.CRETACEOUS, Status.EGG, Parc.HAWAII));     // IN_CREATION
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Velo3", "Velociraptor", "Dromaeosauridae", Era.CRETACEOUS, Status.ADULT, Parc.HAWAII)); // CREATED
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Velo4", "Velociraptor", "Dromaeosauridae", Era.CRETACEOUS, Status.BABY, Parc.HAWAII));  // CREATED
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Stego1", "Stegosaurus", "Stegosauridae", Era.JURASSIC, Status.ADULT, Parc.HAWAII));     // CREATED
        dinos.add(new DinoWithTypeDto(UUID.randomUUID(), "Stego2", "Stegosaurus", "Stegosauridae", Era.JURASSIC, Status.BABY, Parc.HAWAII));      // CREATED
    }

    // Méthodes helper pour déterminer la famille par défaut basée sur l'espèce
    private String getDefaultFamily(String species) {
        return switch (species) {
            case "T-Rex" -> "Tyrannosauridae";
            case "Triceratops" -> "Ceratopsidae";
            case "Velociraptor" -> "Dromaeosauridae";
            case "Stegosaurus" -> "Stegosauridae";
            default -> "Unknown";
        };
    }

    // Méthodes helper pour déterminer l'ère par défaut basée sur l'espèce
    private Era getDefaultEra(String species) {
        return switch (species) {
            case "T-Rex", "Triceratops", "Velociraptor" -> Era.CRETACEOUS;
            case "Stegosaurus" -> Era.JURASSIC;
            default -> Era.CRETACEOUS;
        };
    }
}
