package ofos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/api/users").permitAll() // Allow unrestricted access to /api/users
//                        .requestMatchers("/api/users/username/{username}").permitAll() // Allow unrestricted access to /api/users/user
//                        .requestMatchers("/api/users/id/{id}").permitAll() // Allow unrestricted access to /api/users/id
//                        .requestMatchers("/api/users/create").permitAll() // Allow unrestricted access to /api/users/create
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());      // Sallii POST metodin käytön. Varmaan fiksumpi tapa tehdä
        return http.build();
    }
}