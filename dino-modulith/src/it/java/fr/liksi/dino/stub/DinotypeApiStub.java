package fr.liksi.dino.stub;

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

    public DinotypeApiStub() {
        // Initialisation avec des données de test
        initializeTestData();
    }

    private void initializeTestData() {
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

    @Override
    public List<DinotypeRecordDto> getAllDinotypes() {
        return dinotypes;
    }

    @Override
    public void addDinotype(DinotypeRecordDto dinotypeRecordDto) {
        dinotypes.add(dinotypeRecordDto);
    }
}
