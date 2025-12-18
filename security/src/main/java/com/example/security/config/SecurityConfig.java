package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .requestMatchers("/", "/sample/guest").permitAll()
                // 2. MEMBER 권한이 있어야 함 (hasRole)
                .requestMatchers("/sample/member").hasRole("MEMBER")
                // 3. ADMIN 권한이 있어야 함 (hasRole)
                .requestMatchers("/sample/admin").hasRole("ADMIN")
                // 4. 그 외 나머지 요청은 인증만 되면 허용
                .anyRequest().authenticated()) // 어떤 요청이던 인증을 거쳐라
                // .httpBasic(Customizer.withDefaults()); //http basic인증을 사용
                .formLogin(login -> login.loginPage("/sample/login").permitAll())
                .logout(logout -> logout.logoutUrl("/sample/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        // 사이트 form 인증을 사용

        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico", "/error", "/.well-known/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user1")
                .password("{bcrypt}$2a$10$oLr4dWlerred6m5V8VBbX.ON8JRr/dkHpCTf3snpj0F5OA0FpQh6C")
                .roles("MEMBER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$oLr4dWlerred6m5V8VBbX.ON8JRr/dkHpCTf3snpj0F5OA0FpQh6C")
                .roles("MEMBER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
