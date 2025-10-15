package fr.liksi.dino.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.dto.Status;
import fr.liksi.shared.config.headers.HeadersHolder;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@WebMvcTest(DinoController.class)
class DinoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DinoController dinoController;

    @MockitoBean
    private HeadersHolder headersHolder;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "unLabo", roles = {"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/dinos").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "unAdmin", roles = {"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnPostDino_shouldSucceedWith200() throws Exception {
        final var dino = new DinoRecordDto(null, "dinoTest", "familyTest", Status.ADN, Parc.HAWAII);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/dino").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dino))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Parc", Parc.HAWAII))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
