package org.lessons.milestone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests

                // Permetti l'accesso pubblico alle API dei ticket
                .requestMatchers("/api/tickets/**").permitAll()

                // Permetti l'accesso pubblico alle risorse statiche (mi dava fastidiosi warning ed errori in console)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                // Permetti l'accesso solo agli amministratori per la creazione e modifica dei ticket
                .requestMatchers("/tickets/create", "/tickets/edit/**").hasAuthority("ROLE_ADMIN")

                // Permetti l'accesso agli operatori e amministratori per visualizzare i ticket
                .requestMatchers("/tickets/**").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")

                // Permetti l'accesso agli operatori e amministratori per aggiungere note ai ticket
                .requestMatchers(HttpMethod.POST, "/tickets/{id}/addNote").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")

                // Le altre richieste devono essere autenticate
                .anyRequest().authenticated())
                .formLogin(login -> login
                        .successHandler(customSuccessHandler()) // Gestione del redirect dopo il login
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") 
                        .logoutSuccessUrl("/login?logout") 
                        .invalidateHttpSession(true) 
                        .clearAuthentication(true) 
                        .permitAll())
                .exceptionHandling(withDefaults())
                .csrf(csrf -> csrf.disable()); 

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String redirectUrl = "/"; 

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin/dashboard"; // Se admin, va alla dashboard admin
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_OPERATOR"))) {
                redirectUrl = "/operator/details"; // Se operatore, va alla dashboard operatore
            }

            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService(); 
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder()); 
        return authProvider;
    }
}
