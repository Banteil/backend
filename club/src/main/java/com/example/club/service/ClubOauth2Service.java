package com.example.club.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.club.dto.MemberDTO;
import com.example.club.entity.Member;
import com.example.club.entity.constant.ClubMemberRole;
import com.example.club.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@Setter
@RequiredArgsConstructor
@Service
public class ClubOauth2Service extends DefaultOAuth2UserService {
    private final MemberRepository mR;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글에서 제공하는 정보 (이메일 등) 추출
        log.info("구글 정보 : " + oAuth2User);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // DB에서 확인 후 없으면 가입, 있으면 조회
        Member member = saveSocialMember(email, name);

        // 기존에 만든 MemberDTO 객체로 변환하여 반환
        return new MemberDTO(member, oAuth2User.getAttributes());
    }

    private Member saveSocialMember(String email, String name) {
        return mR.findByEmailAndFromSocial(email, true)
                .orElseGet(() -> {
                    Member member = Member.builder()
                            .email(email)
                            .name(name)
                            .password("social")
                            .fromSocial(true)
                            .build();
                    member.addMemberRole(ClubMemberRole.USER);
                    return mR.save(member);
                });
    }

}
