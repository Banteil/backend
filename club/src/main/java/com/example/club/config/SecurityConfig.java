package com.example.club.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.log4j.Log4j2;

@EnableWebSecurity // 모든 웹 요청에 대해 Securty Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {
    // 시큐리티 설정 클래스
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // 1. 모두에게 개방 (permitAll)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/assets/**").permitAll()
                .requestMatchers("/", "/member/guest").permitAll()
                // 2. MEMBER 권한이 있어야 함 (hasRole)
                .requestMatchers("/member/member").hasRole("MEMBER")
                // 3. ADMIN 권한이 있어야 함 (hasRole)
                .requestMatchers("/member/admin").hasRole("ADMIN")
                // 4. 그 외 나머지 요청은 인증만 되면 허용
                .anyRequest().authenticated()) // 어떤 요청이던 인증을 거쳐라
                // .httpBasic(Customizer.withDefaults()); //http basic인증을 사용
                .formLogin(login -> login.loginPage("/member/login").defaultSuccessUrl("/", true).permitAll())
                .logout(logout -> logout.logoutUrl("/member/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        // 사이트 form 인증을 사용

        return http.build();
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
