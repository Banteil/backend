package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    @Transactional
    public void readTest() {
        Team result = teamRepository.findById(1L).get();
        System.out.println(result);
        System.out.println(result.getMembers());
    }

    @Test
    public void readAllTest() {
        List<Team> results = teamRepository.findAll();
        results.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    @Test
    @Transactional
    public void readMemberTest() {
        Team result = teamRepository.findById(1L).get();
        System.out.println(result);
        // System.out.println(result.getMembers());
    }

    @Test
    public void deleteTest() {
        teamRepository.deleteById(2L);
    }

    @Test
    public void updateTest() {
        Optional<Team> result = teamRepository.findById(1L);
        Team team = result.get();
        // insert(c), update(u) 작업 시 호출
        teamRepository.save(team);
    }

    @Test
    public void insertTest() {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Team team = Team.builder()
                    .name("Team " + i)
                    .build();
            teams.add(team);
        }

        teamRepository.saveAll(teams);
    }

    @Test
    @Transactional
    @Commit
    public void insertCascadeTest() {
        Team t = Team.builder().name("new").build();
        t.getMembers().add(TeamMember.builder().name("강감찬").team(t).build());
        teamRepository.save(t);
    }

    @Test
    @Transactional
    @Commit
    public void updateCascadeTest() {
        Team t = teamRepository.findById(22L).get();
        TeamMember tM = t.getMembers().get(0);
        tM.setName("홍시루");
    }
}
