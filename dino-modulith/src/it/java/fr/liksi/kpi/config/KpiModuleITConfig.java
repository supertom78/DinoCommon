package fr.liksi.kpi.config;

import fr.liksi.dino.DinoApi;
import fr.liksi.dinotype.DinotypeApi;
import fr.liksi.kpi.stub.DinoApiStub;
import fr.liksi.kpi.stub.DinotypeApiStub;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@Import({DinotypeApiStub.class, DinoApiStub.class})
public class KpiModuleITConfig {

    @Bean
    @Primary
    public DinoApi dinoApiStub() {
        return new DinoApiStub();
    }

    @Bean
    @Primary
    public DinotypeApi dinotypeApiStub() {
        return new DinotypeApiStub();
    }
}
