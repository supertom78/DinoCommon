package fr.liksi.dinolabo.configuration.ext;

import fr.liksi.dinolabo.configuration.ext.model.ApiProperties;
import fr.liksi.dinolabo.configuration.headers.HeadersClientInterceptor;
import fr.liksi.dinorepo.ApiClient;
import fr.liksi.dinorepo.api.DinoControllerApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class DinorepoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "api.dinorepo")
    public ApiProperties dinorepoProperties() {
        return new ApiProperties();
    }

    @Bean
    public DinoControllerApi dinorepoControllerApi(ApiClient apiClientDinolabo) {
        return new DinoControllerApi(apiClientDinolabo);
    }

    @Bean
    @Primary
    public ApiClient apiClientDinorepo(ApiProperties dinorepoProperties,
                                       RestTemplateBuilder restTemplateBuilder,
                                       HeadersClientInterceptor headersClientInterceptor) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme(dinorepoProperties.getScheme())
                .host(dinorepoProperties.getHost())
                .port(dinorepoProperties.getPort())
                .build();
        RestTemplate restTemplate = restTemplateBuilder
                .interceptors(headersClientInterceptor)
                .build();

        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(uri.toUriString());
        return apiClient;
    }

}
