package fr.liksi.dino;

import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.DinoWithTypeDto;
import fr.liksi.dino.dto.Status;
import fr.liksi.dino.repository.DinoRepository;
import fr.liksi.dino.repository.model.Dino;
import fr.liksi.dinotype.repository.DinotypeRepository;
import fr.liksi.dinotype.repository.model.Dinotype;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test d'intégration "boîte noire" pour le module Dino utilisant @ApplicationModuleTest.
 * Ce test utilise la couche HTTP et la vraie base de données testcontainer sans mocks.
 */
@ApplicationModuleTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DinoModuleIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DinoRepository dinoRepository;

    @Autowired
    private DinotypeRepository dinotypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Parc testParc = Parc.HAWAII;

    @BeforeEach
    void setUp() {
        // Nettoyage de la base de données
        dinoRepository.deleteAll();
        dinotypeRepository.deleteAll();

        // Insertion de types de dinosaures pour les tests
        Dinotype trex = new Dinotype()
                .guid(UUID.randomUUID())
                .species("T-Rex")
                .family("Tyrannosauridae")
                .era(Era.CRETACEOUS)
                .parc(testParc);

        Dinotype triceratops = new Dinotype()
                .guid(UUID.randomUUID())
                .species("Triceratops")
                .family("Ceratopsidae")
                .era(Era.CRETACEOUS)
                .parc(testParc);

        dinotypeRepository.save(trex);
        dinotypeRepository.save(triceratops);
    }

    @Test
    @WithMockUser(roles = {"LABO"})
    void should_retrieve_all_dinos_via_http() throws Exception {
        // Given - Insertion de données de test directement en base
        UUID testDinoId = UUID.randomUUID();
        Dino testDino = new Dino()
                .guid(testDinoId)
                .name("Rex")
                .species("T-Rex")
                .status(Status.ADULT)
                .parc(testParc);

        dinoRepository.save(testDino);

        // When & Then - Appel HTTP et vérification de la réponse
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].guid", is(testDinoId.toString())))
                .andExpect(jsonPath("$[0].name", is("Rex")))
                .andExpect(jsonPath("$[0].species", is("T-Rex")))
                .andExpect(jsonPath("$[0].family", is("Tyrannosauridae")))
                .andExpect(jsonPath("$[0].era", is("CRETACEOUS")))
                .andExpect(jsonPath("$[0].status", is("ADULT")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(roles = {"LABO"})
    void should_handle_dino_without_matching_type_via_http() throws Exception {
        // Given - Dinosaure avec une espèce non reconnue
        UUID testDinoId = UUID.randomUUID();
        Dino unknownDino = new Dino()
                .guid(testDinoId)
                .name("Unknown")
                .species("UnknownSpecies")
                .status(Status.BABY)
                .parc(testParc);

        dinoRepository.save(unknownDino);

        // When & Then - Vérification que le dinosaure est retourné sans informations de type
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].guid", is(testDinoId.toString())))
                .andExpect(jsonPath("$[0].name", is("Unknown")))
                .andExpect(jsonPath("$[0].species", is("UnknownSpecies")))
                .andExpect(jsonPath("$[0].family", is(nullValue())))
                .andExpect(jsonPath("$[0].era", is(nullValue())))
                .andExpect(jsonPath("$[0].status", is("BABY")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
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
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDino)))
                .andExpect(status().isOk());

        // Then - Vérification que le dinosaure a été sauvegardé via GET
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
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
    @WithMockUser(roles = {"MANAGER"})
    void should_add_dino_with_provided_guid_via_http() throws Exception {
        // Given
        UUID providedGuid = UUID.randomUUID();
        DinoRecordDto newDino = new DinoRecordDto(
                providedGuid,
                "Blue",
                "Velociraptor",
                Status.ADULT,
                testParc
        );

        // When
        mockMvc.perform(post("/api/dino")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDino)))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].guid", is(providedGuid.toString())))
                .andExpect(jsonPath("$[0].name", is("Blue")))
                .andExpect(jsonPath("$[0].species", is("Velociraptor")))
                .andExpect(jsonPath("$[0].status", is("ADULT")));
    }

    @Test
    @WithMockUser(roles = {"LABO"})
    void should_filter_dinos_by_parc_via_http() throws Exception {
        // Given - Insertion de dinosaures dans différents parcs
        Dino dinoInCorrectParc = new Dino()
                .guid(UUID.randomUUID())
                .name("Rex1")
                .species("T-Rex")
                .status(Status.ADULT)
                .parc(testParc);

        Dino dinoInOtherParc = new Dino()
                .guid(UUID.randomUUID())
                .name("Rex2")
                .species("T-Rex")
                .status(Status.ADULT)
                .parc(Parc.BELLEILE);

        dinoRepository.save(dinoInCorrectParc);
        dinoRepository.save(dinoInOtherParc);

        // When & Then - Seul le dinosaure du bon parc doit être retourné
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Rex1")))
                .andExpect(jsonPath("$[0].parc", is(testParc.name())));
    }

    @Test
    @WithMockUser(roles = {"USER"}) // Rôle insuffisant
    void should_deny_access_with_insufficient_role() throws Exception {
        // When & Then - L'accès doit être refusé
        mockMvc.perform(get("/api/dinos")
                .header("X-Parc", testParc.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
