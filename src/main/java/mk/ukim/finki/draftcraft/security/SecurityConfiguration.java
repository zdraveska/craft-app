package mk.ukim.finki.draftcraft.security;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.security.jwt.JwtAuthenticationFilter;
import mk.ukim.finki.draftcraft.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtTokenFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
//                        request.requestMatchers("/api/account-requests").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/users/request-reset-password").permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/api/users/request-password").permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/api/account-requests/confirm-email").permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/api/account-requests/{id}").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/users/password-complexity").permitAll()
//                                .anyRequest().authenticated())
                .securityContext((securityContext) -> securityContext
                        .requireExplicitSave(true))
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

}