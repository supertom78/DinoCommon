package fr.liksi.dinorepo.configuration.headers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    private final HeadersHolder headersHolder;

    public CustomWebMvcConfigurer(HeadersHolder headersHolder) {
        this.headersHolder = headersHolder;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderHandlerInterceptor(this.headersHolder));
    }
}
