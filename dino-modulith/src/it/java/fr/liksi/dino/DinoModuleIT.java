package fr.liksi.dino;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dino.config.DinoModuleITConfig;
import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.Status;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration "boîte noire" pour le module Dino utilisant @ApplicationModuleTest.
 * Ce test utilise la couche HTTP et la vraie base de données testcontainer sans mocks.
 * Les données de test sont chargées via des scripts SQL pour respecter l'encapsulation du module.
 * Les dinotypes sont fournis par le stub DinotypeApiStub sans manipulation directe de leur table.
 */
@ApplicationModuleTest(extraIncludes = "shared")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DinoModuleITConfig.class)
class DinoModuleIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Parc testParc = Parc.HAWAII;

    @Test
    @WithMockUser(username = "unLabo", roles = {"LABO"})
    @Sql(scripts = "/dino/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/dino/insert-test-dino.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_retrieve_all_dinos_via_http() throws Exception {
        // When & Then - Appel HTTP et vérification de la réponse
        mockMvc.perform(get("/api/dinos")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Rex")))
                .andExpect(jsonPath("$[0].species", is("T-Rex")))
                .andExpect(jsonPath("$[0].family", is("Tyrannosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].status", is("ADULT")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(username = "unLabo", roles = {"LABO"})
    @Sql(scripts = "/dino/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_add_new_dino_via_http() throws Exception {
        // Given
        DinoRecordDto newDino = new DinoRecordDto(
                null, // GUID sera généré automatiquement
                "Tri",
                "Triceratops",
                Status.EGG,
                testParc
        );

        // When - Envoi de la requête POST
        mockMvc.perform(post("/api/dino")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDino))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // Then - Vérification que le dinosaure a été sauvegardé via GET
        mockMvc.perform(get("/api/dinos")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Tri")))
                .andExpect(jsonPath("$[0].species", is("Triceratops")))
                .andExpect(jsonPath("$[0].family", is("Ceratopsidae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].status", is("EGG")))
                .andExpect(jsonPath("$[0].guid", notNullValue()));
    }

    @Test
    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    @Sql(scripts = "/dino/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_add_dino_with_provided_guid_via_http() throws Exception {
        // Given
        UUID providedGuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        DinoRecordDto newDino = new DinoRecordDto(
                providedGuid,
                "Blue",
                "Velociraptor",
                Status.ADULT,
                testParc
        );

        // When
        mockMvc.perform(post("/api/dino")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDino))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/dinos")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].guid", is(providedGuid.toString())))
                .andExpect(jsonPath("$[0].name", is("Blue")))
                .andExpect(jsonPath("$[0].species", is("Velociraptor")))
                .andExpect(jsonPath("$[0].family", is("Dromaeosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].status", is("ADULT")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(username = "unLabo", roles = {"LABO"})
    @Sql(scripts = "/dino/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/dino/insert-dinos-multiple-parcs.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_filter_dinos_by_parc_via_http() throws Exception {

        // When & Then - Seul le dinosaure du bon parc doit être retourné
        mockMvc.perform(get("/api/dinos")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Rex1")))
                .andExpect(jsonPath("$[0].species", is("T-Rex")))
                .andExpect(jsonPath("$[0].family", is("Tyrannosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].status", is("ADULT")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }
}
