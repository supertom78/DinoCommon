package fr.liksi.dino.config;

import fr.liksi.dino.stub.DinotypeApiStub;
import fr.liksi.dinotype.DinotypeApi;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@Import(DinotypeApiStub.class)
public class DinoModuleITConfig {
    
    @Bean
    @Primary
    public DinotypeApi dinotypeApi() {
        return new DinotypeApiStub();
    }
}
