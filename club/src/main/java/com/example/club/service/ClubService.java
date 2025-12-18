package com.example.club.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.club.dto.MemberDTO;
import com.example.club.entity.Member;
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
public class ClubService implements UserDetailsService {
    private final MemberRepository mR;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("clubserivce username {}", username);
        Member member = mR.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new MemberDTO(member);
    }

}
