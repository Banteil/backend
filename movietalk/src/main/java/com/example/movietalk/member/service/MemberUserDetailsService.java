package com.example.movietalk.member.service;

import com.example.movietalk.member.dto.MemberAuthDTO;
import com.example.movietalk.member.dto.MemberDTO;
import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.entity.constant.Role;
import com.example.movietalk.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(MemberDTO dto) {
        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        member.setRole(Role.MEMBER);
        return memberRepository.save(member).getMid();
    }

    @Transactional
    public void modifyNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        member.setNickname(nickname); // Member 엔티티에 메서드 추가 필요
    }

    @Transactional
    public void modifyPassword(String email, String currentPassword, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 체크
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 인코딩 후 저장
        member.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberUserDetailsService loadUserByUsername: " + username);

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email or Social"));

        log.info("Member found: " + member);

        return new MemberAuthDTO(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getRole().name());
    }
}