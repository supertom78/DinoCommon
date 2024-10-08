package fr.liksi.dinogateway.configuration.ext;

import fr.liksi.dinogateway.configuration.ext.model.ApiProperties;
import fr.liksi.dinolabo.ApiClient;
import fr.liksi.dinolabo.api.DinoControllerApi;
import fr.liksi.starters.headers.HeadersClientInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class DinolaboConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "api.dinolabo")
    public ApiProperties dinolaboProperties() {
        return new ApiProperties();
    }

    @Bean
    public DinoControllerApi dinolaboControllerApi(ApiClient apiClientDinolabo) {
        return new DinoControllerApi(apiClientDinolabo);
    }

    @Bean
    @Primary
    public ApiClient apiClientDinolabo(ApiProperties dinolaboProperties,
                                       RestTemplateBuilder restTemplateBuilder,
                                       HeadersClientInterceptor headersClientInterceptor) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme(dinolaboProperties.getScheme())
                .host(dinolaboProperties.getHost())
                .port(dinolaboProperties.getPort())
                .build();
        RestTemplate restTemplate = restTemplateBuilder
                .interceptors(headersClientInterceptor)
                .build();

        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(uri.toUriString());
        return apiClient;
    }

}
