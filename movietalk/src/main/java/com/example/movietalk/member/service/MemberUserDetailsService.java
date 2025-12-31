package com.example.movietalk.member.service;

import com.example.movietalk.member.dto.MemberAuthDTO;
import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

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