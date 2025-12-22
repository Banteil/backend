package com.example.board.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.board.member.dto.MemberDTO;
import com.example.board.member.dto.MemberRegisterDTO;
import com.example.board.member.entity.Member;
import com.example.board.member.entity.constant.MemberRole;
import com.example.board.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@Setter
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository mR;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("clubserivce username {}", username);
        Member member = mR.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new MemberDTO(member);
    }

    public String register(MemberRegisterDTO dto) {
        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fromSocial(false)
                .build();

        member.addMemberRole(MemberRole.USER);

        return mR.save(member).getEmail();
    }
}
