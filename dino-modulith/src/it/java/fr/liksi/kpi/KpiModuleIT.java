package fr.liksi.kpi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.kpi.config.KpiModuleITConfig;
import fr.liksi.kpi.stub.DinoApiStub;
import fr.liksi.kpi.stub.DinotypeApiStub;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration "boîte noire" pour le module KPI utilisant @ApplicationModuleTest.
 * Ce test utilise la couche HTTP et des stubs pour simuler les dépendances vers les modules dino et dinotype.
 * Les données de test sont configurées via les stubs pour respecter l'encapsulation du module.
 */
@ApplicationModuleTest(extraIncludes = "shared")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(KpiModuleITConfig.class)
class KpiModuleIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DinoApiStub dinoApiStub;

    @Autowired
    private DinotypeApiStub dinotypeApiStub;

    private final Parc testParc = Parc.HAWAII;

    @BeforeEach
    void setUp() {
        // Nettoyer les données avant chaque test
        dinoApiStub.clearData();
        dinotypeApiStub.clearData();
    }

    @Test
    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    void should_retrieve_kpis_with_manager_role() throws Exception {
        // Given - Données de test avec 2 TO_CREATE, 1 IN_CREATION, 2 CREATED
        dinotypeApiStub.initializeTestDataForBasicKpis(); // 4 dinotypes
        dinoApiStub.addTestDinosForBasicKpis(); // 1 ADN + 2 ADULT/BABY

        // When & Then - Appel HTTP et vérification de la réponse des KPIs
        mockMvc.perform(get("/api/kpis")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbDinoToCreate", is(2)))
                .andExpect(jsonPath("$.nbDinoInCreation", is(1)))
                .andExpect(jsonPath("$.nbDinoCreated", is(2)));
    }

    @Test
    @WithMockUser(username = "unAdmin", roles = {"ADMIN"})
    void should_retrieve_kpis_with_admin_role() throws Exception {
        // Given - Données de test identiques pour vérifier l'accès ADMIN
        dinotypeApiStub.initializeTestDataForBasicKpis(); // 4 dinotypes
        dinoApiStub.addTestDinosForBasicKpis(); // 1 ADN + 2 ADULT/BABY

        // When & Then - L'accès doit être autorisé pour le rôle ADMIN
        mockMvc.perform(get("/api/kpis")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbDinoToCreate", is(2)))
                .andExpect(jsonPath("$.nbDinoInCreation", is(1)))
                .andExpect(jsonPath("$.nbDinoCreated", is(2)));
    }

    @Test
    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    void should_return_zero_kpis_when_no_dinos() throws Exception {
        // Given - Aucune donnée de test (stubs vides)

        // When & Then - Aucun dinosaure en base, tous les KPIs doivent être à 0
        mockMvc.perform(get("/api/kpis")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbDinoToCreate", is(0)))
                .andExpect(jsonPath("$.nbDinoInCreation", is(0)))
                .andExpect(jsonPath("$.nbDinoCreated", is(0)));
    }

    @Test
    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    void should_calculate_kpis_with_various_dino_statuses() throws Exception {
        // Given - Données de test avec 3 TO_CREATE, 2 IN_CREATION, 4 CREATED
        dinotypeApiStub.initializeTestDataForMultipleStatuses(); // 7 dinotypes
        dinoApiStub.addTestDinosForMultipleStatuses(); // 2 ADN/EGG + 4 ADULT/BABY

        // When & Then - Vérification du calcul des KPIs avec différents statuts
        mockMvc.perform(get("/api/kpis")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbDinoToCreate", is(3)))
                .andExpect(jsonPath("$.nbDinoInCreation", is(2)))
                .andExpect(jsonPath("$.nbDinoCreated", is(4)));
    }
}
