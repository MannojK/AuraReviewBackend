package org.project.social.socailmediadb.security;


import lombok.RequiredArgsConstructor;
import org.project.social.socailmediadb.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class AuraReviewSecurityConfig {
    private final List<String> publicPaths;

 @Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {

    return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(requests -> {
                requests.requestMatchers("/api/v1/auth/**").permitAll();

                publicPaths.forEach(path ->
                        requests.requestMatchers(path).permitAll()
                );

                requests.anyRequest().authenticated();
            })
            .addFilterBefore(
                    new JWTTokenValidatorFilter(publicPaths),
                    BasicAuthenticationFilter.class
            )
            .build();
}

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
//            throws Exception {
//        return http.csrf(csrfConfig -> csrfConfig.
//                        csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
//                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests((requests) -> {
//                            publicPaths.forEach(path ->
//                                    requests.requestMatchers(path).permitAll()
//                                            .requestMatchers("/api/v1/auth/**").permitAll()
//                            );
//
//                            requests.anyRequest().authenticated();
//                        }
//                )
//                .addFilterBefore(new JWTTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class)
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults()).build();
//    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider) {
        var providerManager = new ProviderManager(authenticationProvider);
        return providerManager;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Allows passwords prefixed with {noop} in the DB, e.g., "{noop}techguru123"
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "https://aura-review-frontend-inslkdu3v-personal-5157.vercel.app",
                    "https://aura-review-frontend-git-main-personal-5157.vercel.app",
                    "https://aura-review-frontend.vercel.app"
));

        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}