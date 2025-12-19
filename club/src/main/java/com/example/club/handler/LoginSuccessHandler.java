package com.example.club.handler;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 후 경로 지정
        // ROLE_USER => /member/profile
        // ROLE_MANAGER => /manager/info
        // ROLE_ADMIN => /admin/manage

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/manage");
        } else if (roles.contains("ROLE_MANAGER")) {
            response.sendRedirect("/manager/info");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/member/profile");
        } else {
            response.sendRedirect("/");
        }
    }

}
