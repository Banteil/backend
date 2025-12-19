package com.example.club.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.club.entity.Member;
import com.example.club.entity.constant.ClubMemberRole;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository mR;
    @Autowired
    private PasswordEncoder pE;

    @Test
    public void insertTest() {
        List<Member> mList = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member m = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password(pE.encode("1111"))
                    .name("user" + i)
                    .fromSocial(false)
                    .build();
            m.addMemberRole(ClubMemberRole.USER);
            if (i > 8) {
                m.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 9) {
                m.addMemberRole(ClubMemberRole.ADMIN);
            }
            mList.add(m);
        });
        mR.saveAll(mList);
    }

    @Test
    public void testLogin() {
        Member member = mR.findByEmailAndFromSocial("user10@gmail.com", false).get();
        System.out.println(member);
    }
}
