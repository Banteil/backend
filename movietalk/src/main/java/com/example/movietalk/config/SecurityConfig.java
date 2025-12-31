package com.example.movietalk.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.RememberMeServices;
// import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
// import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import com.example.movietalk.member.handler.CustomAuthFailureHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@EnableMethodSecurity
@EnableWebSecurity // 모든 웹 요청에 대해 Securty Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomAuthFailureHandler customAuthFailureHandler;
        // private final MemberOauth2Service clubOauth2Service;

        // SecurityConfig(MemberOauth2Service clubOauth2Service) {
        // this.clubOauth2Service = clubOauth2Service;
        // }

        // 시큐리티 설정 클래스
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                                // .requestMatchers("/movie/create", "/movie/modify",
                                // "/movie/remove").hasRole("ADMIN")
                                .requestMatchers("/assets/**", "/css/**", "/js/**").permitAll()
                                .anyRequest().permitAll());

                // 폼 로그인 설정 활성화
                http.formLogin(login -> login
                                .loginPage("/member/login")
                                .loginProcessingUrl("/login") // HTML form action과 일치해야 함
                                .defaultSuccessUrl("/movie/list")
                                .failureHandler(customAuthFailureHandler)
                                .permitAll());

                http.logout(logout -> logout
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/movie/list")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID"));

                http.csrf(csrf -> csrf.disable());

                return http.build();
        }

        // @Bean
        // RememberMeServices rememberMeServices(UserDetailsService userDetailsService)
        // {
        // RememberMeTokenAlgorithm eTokenAlgorithm = RememberMeTokenAlgorithm.SHA256;
        // TokenBasedRememberMeServices services = new
        // TokenBasedRememberMeServices("myKey", userDetailsService,
        // eTokenAlgorithm);
        // services.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        // services.setTokenValiditySeconds(60 * 60 * 24 * 7);
        // return services;
        // }

        // @Bean
        // LoginSuccessHandler loginSuccessHandler() {
        // return new LoginSuccessHandler();
        // }

        @Bean
        WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                .requestMatchers("/favicon.ico", "/error", "/.well-known/**");
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
