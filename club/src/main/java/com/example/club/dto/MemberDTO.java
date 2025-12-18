package com.example.club.dto;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.club.entity.Member;

public class MemberDTO extends User {
    private String email;
    private String password;
    private String name;
    private boolean fromSocial;

    public MemberDTO(Member member) {
        super(member.getEmail(),
                member.getPassword(),
                member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()));

        this.email = member.getEmail();
        this.name = member.getName();
        this.fromSocial = member.isFromSocial();
    }
}
