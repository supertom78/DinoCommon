package fr.liksi.starters.headers;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@EnableConfigurationProperties(HeadersHolderConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({HeadersConfig.class, ScopeConfig.class,})
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    private final HeadersHolder headersHolder;

    public CustomWebMvcConfigurer(HeadersHolder headersHolder) {
        this.headersHolder = headersHolder;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderHandlerInterceptor(this.headersHolder));
    }
}
