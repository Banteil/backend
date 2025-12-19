package com.example.club.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.club.handler.LoginSuccessHandler;
import com.example.club.service.ClubOauth2Service;
import lombok.extern.log4j.Log4j2;

@EnableWebSecurity // 모든 웹 요청에 대해 Securty Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {

    private final ClubOauth2Service clubOauth2Service;

    SecurityConfig(ClubOauth2Service clubOauth2Service) {
        this.clubOauth2Service = clubOauth2Service;
    }

    // 시큐리티 설정 클래스
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/assets/**").permitAll()
                .requestMatchers("/", "member/auth").permitAll()
                .requestMatchers("/member/manager").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/member/admin").hasRole("ADMIN")
                .anyRequest().authenticated())
                // .httpBasic(Customizer.withDefaults()); //http basic인증을 사용
                .formLogin(login -> login.loginPage("/member/login").permitAll().successHandler(loginSuccessHandler()))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/member/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(clubOauth2Service)))
                .logout(logout -> logout.logoutUrl("/member/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        // 사이트 form 인증을 사용

        return http.build();
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
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }
}
