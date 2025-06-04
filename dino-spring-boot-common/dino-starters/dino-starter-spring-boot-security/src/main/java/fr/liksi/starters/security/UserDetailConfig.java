package fr.liksi.starters.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserDetailConfig {
    @Bean
    @ConditionalOnMissingBean(name="userDetailsService")
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userPaleo1 = User.withUsername("MarcLePaléo")
                .password(passwordEncoder.encode("password"))
                .roles("PALEO")
                .build();

        UserDetails userPaleo2 = User.withUsername("TanguyLePaléo")
                .password(passwordEncoder.encode("password"))
                .roles("PALEO")
                .build();

        UserDetails userMarketing = User.withUsername("HugoDuMarket")
                .password(passwordEncoder.encode("password"))
                .roles("MARKETING")
                .build();

        UserDetails userLabo1 = User.withUsername("ThierryDuLab")
                .password(passwordEncoder.encode("password"))
                .roles("LABO")
                .build();

        UserDetails userLabo2 = User.withUsername("AntoineDuLab")
                .password(passwordEncoder.encode("password"))
                .roles("LABO")
                .build();

        UserDetails userManager1 = User.withUsername("StephaneLeCEO")
                .password(passwordEncoder.encode("password"))
                .roles("MANAGER")
                .build();

        UserDetails userManager2 = User.withUsername("ThomasLeCTO")
                .password(passwordEncoder.encode("password"))
                .roles("MANAGER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("PALEO", "LABO", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userPaleo1, userPaleo2, userMarketing,
                userLabo1, userLabo2,
                userManager1, userManager2,
                admin);
    }

    @Bean
    @ConditionalOnMissingBean(name="passwordEncoder")
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
