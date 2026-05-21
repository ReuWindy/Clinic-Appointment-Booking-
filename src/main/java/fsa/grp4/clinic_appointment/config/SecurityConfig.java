package fsa.grp4.clinic_appointment.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import fsa.grp4.clinic_appointment.security.jwt.JwtAuthenticationEntryPoint;
import fsa.grp4.clinic_appointment.security.jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/api/auth/**",
                                                                "/api/public/**",
                                                                "/actuator/**"

                                                ).permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/admin/specialties")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST")
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                                                .requestMatchers("/api/receptionist/**").hasRole("RECEPTIONIST")
                                                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(
                                "http://localhost:5173",
                                "http://127.0.0.1:5173",
                                "http://localhost:4713",
                                "http://127.0.0.1:4173"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
