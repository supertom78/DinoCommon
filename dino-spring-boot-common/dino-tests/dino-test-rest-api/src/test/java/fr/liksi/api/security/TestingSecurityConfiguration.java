package fr.liksi.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.api.web.TestController;
import fr.liksi.utils.model.Parc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingSecurityConfiguration {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestController testController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnOnlyLabo_shouldKOUnauthorized() throws Exception {
        mvc.perform(get("/test/onlylabo").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnOnlyLabo_shouldKOForbidden() throws Exception {
        mvc.perform(get("/test/onlylabo").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unLabo",roles={"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnOnlyLabo_shouldOK() throws Exception {
        mvc.perform(get("/test/onlylabo").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnOnlyLaboOrAdmin_shouldKOUnauthorized() throws Exception {
        mvc.perform(get("/test/onlylabooradmin").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnOnlyLaboOrAdmin_shouldKOForbidden() throws Exception {
        mvc.perform(get("/test/onlylabooradmin").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unLabo",roles={"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnOnlyLaboOrAdmin_shouldOK() throws Exception {
        mvc.perform(get("/test/onlylabooradmin").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unAdmin",roles={"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnOnlyLaboOrAdmin_shouldOK() throws Exception {
        mvc.perform(get("/test/onlylabooradmin").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }
}
