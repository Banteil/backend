package com.example.movietalk.member.handler;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // 사용자가 로그인을 시도했던 이메일 값을 가져옵니다.
        String username = request.getParameter("username");
        String errorMessage;

        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다.";
        } else {
            errorMessage = "로그인에 실패하였습니다. 다시 시도해주세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        // URL에 username 파라미터를 추가합니다.
        setDefaultFailureUrl("/member/login?error=true&exception=" + errorMessage + "&username=" + username);

        super.onAuthenticationFailure(request, response, exception);
    }
}
