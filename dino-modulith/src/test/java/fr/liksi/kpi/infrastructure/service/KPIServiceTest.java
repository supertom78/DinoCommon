package fr.liksi.kpi.infrastructure.service;

import fr.liksi.kpi.dto.Kpis;
import fr.liksi.kpi.domain.api.CreateKpis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KPIServiceTest {

    @Mock
    private CreateKpis createKpis;

    private KPIService kpiService;

    @BeforeEach
    void setUp() {
        kpiService = new KPIService(createKpis);
    }

    @Test
    void getAllKpis_shouldReturnConsolidatedKpis() {
        // Arrange
        var expectedKpis = new fr.liksi.kpi.domain.api.model.Kpis(1, 2, 3);
        when(createKpis.consolidate()).thenReturn(expectedKpis);

        // Act
        Kpis result = kpiService.getAllKpis();

        // Assert
        assertEquals(expectedKpis.nbDinoToCreate(), result.nbDinoToCreate());
        assertEquals(expectedKpis.nbDinoCreated(), result.nbDinoCreated());
        assertEquals(expectedKpis.nbDinoInCreation(), result.nbDinoInCreation());
        verify(createKpis).consolidate();
    }

}
