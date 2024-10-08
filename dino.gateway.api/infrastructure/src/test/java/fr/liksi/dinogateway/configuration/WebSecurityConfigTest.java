package fr.liksi.dinogateway.configuration;

import fr.liksi.dinogateway.api.CreateKpis;
import fr.liksi.dinogateway.web.KPIController;
import fr.liksi.utils.model.Parc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class WebSecurityConfigTest {

    @Autowired
    private KPIController kpiController;

    @MockBean
    private CreateKpis createKpis;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetSwagger_shouldOKWith200() throws Exception {
        mvc.perform(get("/v3/api-docs").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestNoAuthentificationOnGetKpis_shouldKOUnauthorized() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnGetKpis_shouldKOForbidden() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unMarket",roles={"MARKETING"})
    @Test
    public void givenAuthRequestWithMarketingOnGetKpis_shouldKOForbidden() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unLabo",roles={"LABO"})
    @Test
    public void givenAuthRequestWithLaboOnGetKpis_shouldKOForbidden() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="unManager",roles={"MANAGER"})
    @Test
    public void givenAuthRequestWithManagerOnGetKpis_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="unAdmin",roles={"ADMIN"})
    @Test
    public void givenAuthRequestWithAdminOnGetKpis_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }

}
