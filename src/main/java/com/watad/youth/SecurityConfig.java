package com.watad.youth;

import com.watad.security.CustomAuthenticationFailureHandle;
import com.watad.security.JpaPersistentTokenRepository;
import com.watad.security.events.LoginSuccessHandler;
import com.watad.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JpaPersistentTokenRepository tokenRepo) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin(form -> form
                        .loginPage("/sign-in")
                        .successHandler(loginSuccessHandler)
                        .loginProcessingUrl("/login")
                        .failureHandler(new CustomAuthenticationFailureHandle(userDetailsService,passwordEncoder()))
                        .permitAll()

                ).rememberMe(remember ->remember
                        .tokenRepository(tokenRepo)
                        .tokenValiditySeconds(60*60*24*365).key("wazna@2022")
                        .userDetailsService(userDetailsService)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/sign-in")
                        .permitAll()
                )

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/youth/point/transfer").hasAnyRole("YOUTH")
                                .requestMatchers("/youth/point/details").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/youth/point/**").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/profile").authenticated()
                                .requestMatchers("/profile/**").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/youth/point/transfer").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/manualAttandance").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/bonus/**").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/event/**").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/qr/**").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                                .requestMatchers("/api/role").hasAnyRole("YOUTH","CLASS_LEADER","TREASURER","SERVER","SERVICE_LEADER")
                        .requestMatchers(
                                "/sign-in",
                                "/register",
                                "/awaitingApprove",
                                "/editProfile/**",
                                "/css/**",
                                "/js/**",
                                "/api/scan/code/**",
                                "/api/church/**",
                                "/api/meeting/**",
                                "/api/periest/**",
                                "/uploads/sitePic/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling(config->
                        config.accessDeniedPage("/accessDenied"));

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return  authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}