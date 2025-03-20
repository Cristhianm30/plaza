package com.pragma.powerup.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
                                "/error",
                                "/swagger-ui/**",
                                "/swagger-ui.**",
                                "/v3/api-docs/**"

                        ).permitAll()
                        .antMatchers(HttpMethod.POST, "/restaurants").hasRole("ADMINISTRADOR")
                        .antMatchers(HttpMethod.POST, "/dishes","/dishes/update/{id}","dishes/active/{id}","/restaurants/{restaurantId}/employee").hasRole("PROPIETARIO")
                        .antMatchers(HttpMethod.GET, "/restaurants/list","dishes/restaurant/{restaurantId}","/orders","/orders/cancel/{orderId}","orders/logs/client").hasRole("CLIENTE")
                        .antMatchers(HttpMethod.GET, "/orders/list","/orders/employee/{orderId}","/orders/ready/{orderId}","/orders/deliver/{orderId}").hasRole("EMPLEADO")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
