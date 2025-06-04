package fr.liksi.starters.security;

import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Import({UserDetailConfig.class})
public class WebSecurityConfig {

    @Bean
    @ConditionalOnMissingBean(name="requestAuthorization")
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorization() {
        AccessControlHeaderManager accessControl = new AccessControlHeaderManager();
        return AuthorizationManagers.anyOf(accessControl);
    }

    @Bean
    @ConditionalOnMissingBean(name="filterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> request.anyRequest().access(requestAuthorization()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    @ConditionalOnClass(SwaggerConfig.class)
    @ConditionalOnMissingBean(name = "swaggerFilterChain")
    @Order(HIGHEST_PRECEDENCE + 100)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/v3/api-docs/**", "/swagger-ui/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
