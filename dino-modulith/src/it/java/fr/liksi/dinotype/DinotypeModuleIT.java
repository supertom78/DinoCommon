package fr.liksi.dinotype;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
 * Test d'intégration "boîte noire" pour le module Dinotype utilisant @ApplicationModuleTest.
 * Ce test utilise la couche HTTP et la vraie base de données testcontainer sans mocks.
 * Les données de test sont chargées via des scripts SQL pour respecter l'encapsulation du module.
 */
@ApplicationModuleTest(extraIncludes = "shared")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DinotypeModuleIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Parc testParc = Parc.HAWAII;

    @Test
    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    @Sql(scripts = "/dinotype/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/dinotype/insert-test-dinotypes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_retrieve_all_dinotypes_via_http() throws Exception {
        // When & Then - Appel HTTP et vérification de la réponse
        mockMvc.perform(get("/api/dinotypes")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].species", is("T-Rex")))
                .andExpect(jsonPath("$[0].family", is("Tyrannosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())))
                .andExpect(jsonPath("$[1].species", is("Triceratops")))
                .andExpect(jsonPath("$[1].family", is("Ceratopsidae")))
                .andExpect(jsonPath("$[1].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[2].species", is("Velociraptor")))
                .andExpect(jsonPath("$[2].family", is("Dromaeosauridae")))
                .andExpect(jsonPath("$[2].era", is("CRETACEOUS")));
    }

    @Test
    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    @Sql(scripts = "/dinotype/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_add_new_dinotype_via_http() throws Exception {
        // Given
        DinotypeRecordDto newDinotype = new DinotypeRecordDto(
                null, // GUID sera généré automatiquement
                "Stegosaurus",
                "Stegosauridae",
                Era.JURASSIC,
                testParc
        );

        // When - Envoi de la requête POST
        mockMvc.perform(post("/api/dinotype")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDinotype))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // Then - Vérification que le type de dinosaure a été sauvegardé via GET
        mockMvc.perform(get("/api/dinotypes")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].species", is("Stegosaurus")))
                .andExpect(jsonPath("$[0].family", is("Stegosauridae")))
                .andExpect(jsonPath("$[0].era", is("JURASSIC")))
                .andExpect(jsonPath("$[0].guid", notNullValue()));
    }

    @Test
    @WithMockUser(username = "unAdmin", roles = {"ADMIN"})
    @Sql(scripts = "/dinotype/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_add_dinotype_with_provided_guid_via_http() throws Exception {
        // Given
        UUID providedGuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        DinotypeRecordDto newDinotype = new DinotypeRecordDto(
                providedGuid,
                "Brachiosaurus",
                "Brachiosauridae",
                Era.JURASSIC,
                testParc
        );

        // When
        mockMvc.perform(post("/api/dinotype")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDinotype))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/dinotypes")
                        .header("Parc", testParc.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].guid", is(providedGuid.toString())))
                .andExpect(jsonPath("$[0].species", is("Brachiosaurus")))
                .andExpect(jsonPath("$[0].family", is("Brachiosauridae")))
                .andExpect(jsonPath("$[0].era", is("JURASSIC")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(username = "unMarketing", roles = {"MARKETING"})
    @Sql(scripts = "/dinotype/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/dinotype/insert-dinotypes-multiple-parcs.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_filter_dinotypes_by_parc_via_http() throws Exception {

        // When & Then - Seuls les types de dinosaures du bon parc doivent être retournés
        mockMvc.perform(get("/api/dinotypes")
                        .header("Parc", testParc.name())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].species", is("T-Rex")))
                .andExpect(jsonPath("$[0].family", is("Tyrannosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())))
                .andExpect(jsonPath("$[1].species", is("Triceratops")))
                .andExpect(jsonPath("$[1].family", is("Ceratopsidae")))
                .andExpect(jsonPath("$[1].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[1].parc", is(testParc.name())));
    }

}
