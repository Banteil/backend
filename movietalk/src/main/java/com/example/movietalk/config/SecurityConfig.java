package com.example.movietalk.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.RememberMeServices;
// import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
// import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import lombok.extern.log4j.Log4j2;

@EnableMethodSecurity
@EnableWebSecurity // 모든 웹 요청에 대해 Securty Filter Chain 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {

        // private final MemberOauth2Service clubOauth2Service;

        // SecurityConfig(MemberOauth2Service clubOauth2Service) {
        // this.clubOauth2Service = clubOauth2Service;
        // }

        // 시큐리티 설정 클래스
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                // 1. 인가 설정 (Authorization)
                http.authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll() // 나머지 모든 요청 허용 (메서드 보안 @PreAuthorize 활용 권장)
                );

                // // 2. 폼 로그인 설정 (Form Login)
                // http.formLogin(login -> login
                // .loginPage("/member/login")
                // .loginProcessingUrl("/member/login")
                // .successHandler(loginSuccessHandler())
                // .permitAll());

                // // 3. OAuth2 로그인 설정 (Social Login)
                // http.oauth2Login(oauth2 -> oauth2
                // .loginPage("/member/login")
                // .userInfoEndpoint(userInfo -> userInfo.userService(clubOauth2Service)));

                // // 4. 로그아웃 설정 (Logout)
                // http.logout(logout -> logout
                // .logoutUrl("/member/logout")
                // .logoutSuccessUrl("/")
                // .invalidateHttpSession(true)
                // .deleteCookies("JSESSIONID", "remember-me") // 리멤버미 쿠키도 함께 삭제 권장
                // );

                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

                // // 5. 자동 로그인 설정 (Remember-Me)
                // http.rememberMe(remember -> remember
                // .rememberMeServices(rememberMeServices));

                // 6. CSRF 설정 (필요 시 추가)
                http.csrf(csrf -> csrf.disable()); // REST API 중심이라면 검토

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
