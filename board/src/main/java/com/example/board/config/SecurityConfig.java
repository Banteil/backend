package com.example.board.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import com.example.board.member.handler.LoginSuccessHandler;
import com.example.board.member.service.MemberOauth2Service;
import lombok.extern.log4j.Log4j2;

@EnableMethodSecurity
@EnableWebSecurity // 모든 웹 요청에 대해 Securty Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {

    private final MemberOauth2Service clubOauth2Service;

    SecurityConfig(MemberOauth2Service clubOauth2Service) {
        this.clubOauth2Service = clubOauth2Service;
    }

    // 시큐리티 설정 클래스
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/member/manager").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/member/admin").hasRole("ADMIN")
                .anyRequest().permitAll())
                // .httpBasic(Customizer.withDefaults()); //http basic인증을 사용
                .formLogin(login -> login.loginPage("/member/login").loginProcessingUrl("/member/login")
                        .successHandler(loginSuccessHandler()).permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/member/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(clubOauth2Service)))
                .logout(logout -> logout.logoutUrl("/member/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .rememberMe(remember -> remember.rememberMeServices(rememberMeServices));
        // 사이트 form 인증을 사용

        return http.build();
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        // 토큰 생성용 알고리즘
        RememberMeTokenAlgorithm eTokenAlgorithm = RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("myKey", userDetailsService,
                eTokenAlgorithm);
        services.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        services.setTokenValiditySeconds(60 * 60 * 24 * 7);
        return services;
    }

    @Bean
    LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

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
