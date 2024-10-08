package fr.liksi.dinolabo.configuration;

import fr.liksi.starters.headers.HeadersHolder;
import fr.liksi.utils.model.Parc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HeaderHolderTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private HeadersHolder headersHolder;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username="unPaleo",roles={"PALEO"})
    @Test
    public void givenAuthRequestWithPaleoOnGetAllDinos_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/dinos").contentType(MediaType.APPLICATION_JSON)
                        .header("Parc", Parc.HAWAII));

        assertThat(headersHolder.getParc()).isEqualTo(Parc.HAWAII);
    }
}
