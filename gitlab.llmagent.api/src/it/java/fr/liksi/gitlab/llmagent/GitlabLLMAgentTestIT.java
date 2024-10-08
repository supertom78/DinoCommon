package fr.liksi.gitlab.llmagent;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.liksi.gitlab.llmagent.web.model.GitlabCommitPropagationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitlabLLMAgentTestIT {

    @Value("${langchain4j.temperature}")
    private String temperature;

    @Value("${langchain4j.modelName}")
    private String modelName;

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void should_analyseAndPropaget3Commits_to_dinorepo() throws Exception {
        final var gitlabCommitRequest = new GitlabCommitPropagationRequest(
                "https://source_todo",
                "develop",
                "https://target_todo",
                "develop",
                "test/test"+new SimpleDateFormat("ddMMkkmmss").format(new Date())+"_"+modelName+"_"+temperature,
                "sha_todo");

        mvc.perform(post("/api/propagate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gitlabCommitRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}