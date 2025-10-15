package fr.liksi.kpi.stub;

import fr.liksi.dinotype.DinotypeApi;
import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestComponent
public class DinotypeApiStub implements DinotypeApi {

    private List<DinotypeRecordDto> dinotypes = new ArrayList<>();

    public void setTestData(List<DinotypeRecordDto> testDinotypes) {
        this.dinotypes = new ArrayList<>(testDinotypes);
    }

    public void clearData() {
        this.dinotypes.clear();
    }

    @Override
    public List<DinotypeRecordDto> getAllDinotypes() {
        return new ArrayList<>(dinotypes);
    }

    @Override
    public void addDinotype(DinotypeRecordDto dinotypeRecordDto) {
        // Générer un GUID si null
        DinotypeRecordDto dinotypeToAdd = dinotypeRecordDto.guid() == null
            ? new DinotypeRecordDto(UUID.randomUUID(), dinotypeRecordDto.species(), dinotypeRecordDto.family(), dinotypeRecordDto.era(), dinotypeRecordDto.parc())
            : dinotypeRecordDto;

        dinotypes.add(dinotypeToAdd);
    }

    // Méthodes utilitaires pour les tests
    public void initializeTestData() {
        clearData();
        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "T-Rex",
                "Tyrannosauridae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));

        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Triceratops",
                "Ceratopsidae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));

        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Velociraptor",
                "Dromaeosauridae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));
    }

    public void initializeTestDataForBasicKpis() {
        clearData();
        // Pour nbDinoToCreate=2, avec 2 dinos créés -> il faut 4 dinotypes (4-2=2)
        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "T-Rex",
                "Tyrannosauridae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));

        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Triceratops",
                "Ceratopsidae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));

        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Velociraptor",
                "Dromaeosauridae",
                Era.CRETACEOUS,
                Parc.HAWAII
        ));

        dinotypes.add(new DinotypeRecordDto(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Stegosaurus",
                "Stegosauridae",
                Era.JURASSIC,
                Parc.HAWAII
        ));
    }

    public void initializeTestDataForMultipleStatuses() {
        clearData();
        // Pour nbDinoToCreate=3, avec 4 dinos créés -> il faut 7 dinotypes (7-4=3)
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "T-Rex", "Tyrannosauridae", Era.CRETACEOUS, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Triceratops", "Ceratopsidae", Era.CRETACEOUS, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Velociraptor", "Dromaeosauridae", Era.CRETACEOUS, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Stegosaurus", "Stegosauridae", Era.JURASSIC, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Allosaurus", "Allosauridae", Era.JURASSIC, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Brachiosaurus", "Brachiosauridae", Era.JURASSIC, Parc.HAWAII));
        dinotypes.add(new DinotypeRecordDto(UUID.randomUUID(), "Diplodocus", "Diplodocidae", Era.JURASSIC, Parc.HAWAII));
    }
}
