package fr.liksi.dinorepo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DinorepoApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Test
	void contextLoads() {
	}

	@Test
	public void getSwaggerJsonFile() throws IOException {
		String swagger = new RestTemplate().getForObject("http://localhost:" + randomServerPort + "/v3/api-docs", String.class);
		Path path = Paths.get(System.getProperty("user.dir"), "target", "dinorepo.json");
		FileCopyUtils.copy(swagger.getBytes(StandardCharsets.UTF_8), path.toFile());
	}

}
