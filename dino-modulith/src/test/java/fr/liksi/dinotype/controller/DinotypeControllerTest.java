package fr.liksi.dinotype.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.dinotype.dto.DinotypeRecordDto;
import fr.liksi.dinotype.service.DinoRepoService;
import fr.liksi.shared.config.headers.HeadersHolder;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DinotypeController.class)
class DinotypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DinoRepoService dinotypeService;

    @MockitoBean
    private HeadersHolder headersHolder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    public void givenAuthRequestWithPaleoOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/api/dinotypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    public void givenAuthRequestWithPaleoOnPostDino_shouldSucceedWith200() throws Exception {
        final var dinotype = new DinotypeRecordDto(UUID.randomUUID(), "dinoTest", "familyTest", Era.CRETACEOUS, Parc.HAWAII);
        mockMvc.perform(post("/api/dinotype").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dinotype))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
