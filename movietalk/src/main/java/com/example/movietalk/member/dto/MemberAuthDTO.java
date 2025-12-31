package com.example.movietalk.member.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@ToString
public class MemberAuthDTO extends User {
    private String email;
    private String nickname;

    public MemberAuthDTO(String username, String password, String nickname, String role) {
        super(username, password, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        this.email = username;
        this.nickname = nickname;
    }
}