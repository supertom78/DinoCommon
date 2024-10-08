package fr.liksi.dinorepo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dinorepo.repository.model.DinotypeRecordDto;
import fr.liksi.dinorepo.repository.model.Era;
import fr.liksi.dinorepo.configuration.headers.model.Parc;
import fr.liksi.dinorepo.service.DinoService;
import fr.liksi.dinorepo.web.DinotypeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WebSecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DinotypeController dinotypeController;

    @MockBean
    private DinoService dinoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        ReflectionTestUtils.setField(dinotypeController, "dinoService", dinoService);
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetSwagger_shouldOKWith200() throws Exception {
        mvc.perform(get("/v3/api-docs").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetAllDinos_shouldKOUnauthorized() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unMarket",roles={"MARKETING"})
    @Test
    public void givenAuthRequestWithMarketingOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unLabo",roles={"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unManager",roles={"MANAGER"})
    @Test
    public void givenAuthRequestWithManagerOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unAdmin",roles={"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinotypes").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnPostDino_shouldKOUnauthorized() throws Exception {
        final var dinotype = new DinotypeRecordDto(UUID.randomUUID(), "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(UUID.randomUUID(), "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unMarket",roles={"MARKETING"})
    @Test
    public void givenAuthRequestWithMarketingOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unLabo",roles={"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnOnPostDino_shouldKOForbidden() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unManager",roles={"MANAGER"})
    @Test
    public void givenAuthRequestWithManagerOnPostDino_shouldKOForbidden() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unAdmin",roles={"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(null, "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
