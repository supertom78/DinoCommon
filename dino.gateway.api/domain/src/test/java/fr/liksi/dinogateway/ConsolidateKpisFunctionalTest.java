package fr.liksi.dinogateway;

import fr.liksi.dinogateway.spi.stub.DinoInventoryStub;
import fr.liksi.dinogateway.spi.stub.DinoTypeInventoryStub;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ConsolidateKpisFunctionalTest
{
    @Test
    void should_consolidate_kpis_for_hawaii_parc(){
        //Given
        final var kpiConsolider = new KpisCreator(new DinoInventoryStub(), new DinoTypeInventoryStub());

        //When
        final Kpis kpis = kpiConsolider.consolidate();

        //Then
        assertThat(kpis)
                .matches(k -> k.nbDinoToCreate() == 4 && k.nbDinoCreated() == 1 && k.nbDinoInCreation() == 3);


    }
}