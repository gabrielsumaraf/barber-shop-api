package com.api.barber.config;

import com.api.barber.domain.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/services").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/services").hasRole(UserRole.OWNER.getRole())
                        .requestMatchers(HttpMethod.PUT, "/api/services/{id}").hasRole(UserRole.OWNER.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/api/services/{id}").hasRole(UserRole.OWNER.getRole())
                        .requestMatchers(HttpMethod.GET, "/api/appointments/working-hours").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/appointments/all/history").hasRole(UserRole.OWNER.getRole())
                        .requestMatchers(HttpMethod.GET, "/api/appointments/barbers/history").hasAnyRole(
                                UserRole.BARBER.getRole(),
                                UserRole.OWNER.getRole())
                        .requestMatchers(HttpMethod.GET, "/api/appointments/customers/history").hasAnyRole(
                                UserRole.CUSTOMER.getRole())

                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{id}").hasAnyRole(
                                UserRole.CUSTOMER.getRole(),
                                UserRole.BARBER.getRole(),
                                UserRole.OWNER.getRole())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
