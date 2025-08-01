package com.watad.youth;

import com.watad.security.CustomAuthenticationFailureHandle;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/sign-in")
                        .successHandler(loginSuccessHandler)
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(new CustomAuthenticationFailureHandle(userDetailsService))
                        .permitAll()

                ).rememberMe(config->config.key("IsaacHanna1@2022")
                        .tokenValiditySeconds(3600))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/sign-in?logout=true")
                        .permitAll()
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/youth/point/add").hasAnyRole("SERVER","SUPER")
                                .requestMatchers("/youth/point/transfer").hasAnyRole("YOUTH","SERVER")
                                .requestMatchers("/manualAttandance").hasAnyRole("SERVER","SUPER")
                        .requestMatchers(
                                "/sign-in",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/api/scan/code/**"
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