package fr.liksi.kpi.infrastructure.web;

import fr.liksi.shared.config.headers.HeadersHolder;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(KPIController.class)
class KPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KPIController kpiController;

    @MockitoBean
    private HeadersHolder headersHolder;

    @WithMockUser(username = "unManager", roles = {"MANAGER"})
    @Test
    public void givenAuthRequestWithManagerOnGetKpis_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/api/kpis").contentType(MediaType.APPLICATION_JSON).header("Parc", Parc.HAWAII))
                .andExpect(status().isOk());
    }
}
