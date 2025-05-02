package com.muhu.SocialMediaApi.config;

import com.muhu.SocialMediaApi.config.filter.JwtTokenValidatorFilter;
import com.muhu.SocialMediaApi.exception.CustomAccessDeniedHandler;
import com.muhu.SocialMediaApi.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class ProjectSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(sessionConfig->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers("/api/user/register","/api/user/login").permitAll()
                        .requestMatchers("/api/user/all").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/post/all").hasRole("ADMIN")
                        .requestMatchers("/api/post/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/notif/all").hasRole("ADMIN")
                        .requestMatchers("/api/notif/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/like/all").hasRole("ADMIN")
                        .requestMatchers("/api/like/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/comment/all").hasRole("ADMIN")
                        .requestMatchers("/api/comment/**").hasAnyRole("USER","ADMIN")

        );
        httpSecurity.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class);
        httpSecurity.httpBasic(e->e.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        httpSecurity.exceptionHandling(e->e.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder){
        CustomUsernamePasswordAuthenticationProvider authenticationProvider =
                new CustomUsernamePasswordAuthenticationProvider(userDetailsService,passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}
