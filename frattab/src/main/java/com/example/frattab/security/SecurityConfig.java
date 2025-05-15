package com.example.frattab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/",
                                                                "/login",
                                                                "/members",
                                                                "/members/**",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**",
                                                                "/static/**",
                                                                "/error",
                                                                "/favicon.ico")
                                                .permitAll()
                                                .requestMatchers("/dashboard",
                                                                "/dashboard/*")
                                                .authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureUrl("/login?error"))
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/?logout"));

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                String encodedPassword = passwordEncoder().encode("root");
                System.out.println("Encoded password for 'root': " + encodedPassword);

                UserDetails admin = User.builder()
                                .username("admin@semper-idem.de")
                                .password(encodedPassword)
                                .roles("ADMIN")
                                .build();

                return new InMemoryUserDetailsManager(admin);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}