package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTest {
    @Autowired
    private PasswordEncoder pE;

    @Test
    public void testEncoder() {
        String password = "1111";
        String encodePass = pE.encode(password);
        // {bcrypt}$2a$10$oLr4dWlerred6m5V8VBbX.ON8JRr/dkHpCTf3snpj0F5OA0FpQh6C
        System.out.println("raw : " + password + ", encode : " + encodePass);
        System.out.println(pE.matches(password, encodePass));
        System.out.println(pE.matches("1234", encodePass));
    }
}
