package fr.liksi.starter.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * {@link EnvironmentPostProcessor} that add default YAML {@link PropertySource}s to current {@link ConfigurableEnvironment}.
 */
public class DefaultPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DEFAULT_PROPERTIES_FILES_LOCATION_PATTERN = "classpath*:config/application-dino-*.yml";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Resource[] defaultPropertiesResources = findDefaultPropertiesResources();

            for (Resource defaultPropertiesResource : defaultPropertiesResources) {
                List<PropertySource<?>> propertySourcesInResource = getPropertySourcesFromResource(defaultPropertiesResource);

                for (PropertySource<?> propertySource : propertySourcesInResource) {
                    environment.getPropertySources().addLast(propertySource);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Resource[] findDefaultPropertiesResources() throws IOException {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(DEFAULT_PROPERTIES_FILES_LOCATION_PATTERN);
        // Assure to have a reproductive sort of default properties resources
        Arrays.sort(resources, comparing(Resource::getFilename));
        return resources;
    }

    private List<PropertySource<?>> getPropertySourcesFromResource(Resource resource) throws IOException {
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        return yamlPropertySourceLoader.load("Acme Boot default properties from " + resource.getFilename(), resource);
    }

    private PropertySourcesPropertyResolver getPropertyResolverFor(PropertySource<?> propertySource) {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource);
        return new PropertySourcesPropertyResolver(propertySources);
    }
}
