package com.example.jpa.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.constant.RoleType;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void updateTest()
    {
        Optional<Member> result = memberRepository.findById(1L);

        Member member = result.get();
        result.ifPresent(m -> {
            m.setName("수정");
        });

        // insert(c), update(u) 작업 시 호출
        memberRepository.save(member);
    }
    
    @Test
    public void insertTest()
    {
        Member member = Member.builder()
        .userId("abcd")
        .name("길동")
        .age(20)
        .role(RoleType.MEMBER)
        .description("안녕하세요, 반갑습니다.")
        .build();

        // insert(c), update(u) 작업 시 호출
        memberRepository.save(member);
    }
}
