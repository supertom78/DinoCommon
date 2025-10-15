package fr.liksi.shared.config;

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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void givenAuthRequestNoAuthentificationOnGetSwagger_shouldOKWith200() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetAllDinos_shouldKOUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "unPaleo", roles = {"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnGetAllDinos_shouldKOForbidden() throws Exception {
        mockMvc.perform(get("/api/test").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "unMarket", roles = {"MARKETING"})
    @Test
    public void givenAuthRequestWithMarketingOnGetAllDinos_shouldKOForbidden() throws Exception {
        mockMvc.perform(get("/api/test").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"LABO", "MANAGER", "ADMIN"})
    @WithMockUser
    public void givenAuthRequestWithAuthorizedRole_shouldSucceedWith200(String role) throws Exception {
        mockMvc.perform(get("/api/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII)
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }
}
