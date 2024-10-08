package fr.liksi.dinogateway.configuration;

import fr.liksi.ddd.DomainService;
import fr.liksi.ddd.Stub;
import fr.liksi.dinogateway.Kpis;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {Kpis.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class})},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {Stub.class})})
public class DomainConfiguration {
}
