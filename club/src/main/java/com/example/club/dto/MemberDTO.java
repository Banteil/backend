package com.example.club.dto;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.club.entity.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@ToString
@Log4j2
public class MemberDTO extends User implements OAuth2User {
    private String email;
    private String name;
    private boolean fromSocial;
    private Map<String, Object> attr; // 구글에서 받은 전체 정보 저장용

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

    // 소셜 로그인용 생성자
    public MemberDTO(Member member, Map<String, Object> attr) {
        this(member);
        log.info("소셜 멤버 : " + member);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
