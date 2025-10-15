package fr.liksi.dinotype.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.dinotype.service.DinoRepoService;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class WebSecurityConfigTest {

    @MockitoBean
    private DinoRepoService dinoRepoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void givenAuthRequestNoAuthentificationOnGetSwagger_shouldOKWith200() throws Exception {
        mockMvc.perform(get("/v3/api-docs").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetAllDinos_shouldKOUnauthorized() throws Exception {
        mockMvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"PALEO", "MARKETING", "LABO", "MANAGER", "ADMIN"})
    @WithMockUser
    public void givenAuthRequestWithAuthorizedRoleOnGetAllDinos_shouldSucceedWith200(String role) throws Exception {
        mockMvc.perform(get("/api/dinotypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII)
                        .with(user("user").roles(role)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnPostDino_shouldKOUnauthorized() throws Exception {
        final var dinotype = new DinotypeRecordDto(UUID.randomUUID(), "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(UUID.randomUUID(), "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "unMarket", roles = {"MARKETING"})
    @Test
    public void givenAuthRequestWithMarketingOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "unLabo", roles = {"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnOnPostDino_shouldKOForbidden() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    @Test
    public void givenAuthRequestWithManagerOnPostDino_shouldKOForbidden() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "unAdmin", roles = {"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
