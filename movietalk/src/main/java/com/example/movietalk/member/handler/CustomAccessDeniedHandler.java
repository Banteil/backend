package com.example.movietalk.member.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 응답 타입을 HTML로 설정
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 자바스크립트로 알림창 띄우고 리스트로 이동
        out.println("<script>");
        out.println("alert('해당 페이지에 접근할 권한이 없습니다.');");
        out.println("location.href='/movie/list';");
        out.println("</script>");

        out.flush();
    }
}
