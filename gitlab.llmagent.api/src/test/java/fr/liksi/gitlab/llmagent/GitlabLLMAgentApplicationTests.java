package fr.liksi.gitlab.llmagent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitlabLLMAgentApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Test
	void contextLoads() {
	}

}
