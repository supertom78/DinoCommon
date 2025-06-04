package fr.liksi.starters.headers;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class HeadersConfig {

    @Bean
    @Scope(scopeName = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnMissingBean(name = "headersHolder")
    public HeadersHolder headersHolder(HeadersHolderConfig headersHolderConfig) {
        return new HeadersHolder(headersHolderConfig);
    }

    @Bean
    @ConditionalOnMissingBean(name = "headersClientInterceptor")
    public HeadersClientInterceptor headersClientInterceptor(HeadersHolder headersHolder) {
        return new HeadersClientInterceptor(headersHolder);
    }

}
