package fr.liksi.dinolabo.configuration.headers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class HeadersConfig {

    @Bean
    @Scope(scopeName = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HeadersHolder headersHolder() {
        return new HeadersHolder();
    }

    @Bean
    public HeadersClientInterceptor headersClientInterceptor(HeadersHolder headersHolder) {
        return new HeadersClientInterceptor(headersHolder);
    }

}
