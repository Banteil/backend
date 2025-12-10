package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamMemberRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    @Transactional
    public void readTest() {
        TeamMember result = teamMemberRepository.findById(1L).get();
        System.out.println(result);
        System.out.println(result.getTeam());
    }

    @Test
    public void readAllTest() {
        List<TeamMember> results = teamMemberRepository.findAll();
        results.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    @Test
    public void deleteTest() {
        // studentRepository.delete(null);
        teamMemberRepository.deleteById(2L);
    }

    @Test
    public void updateTest() {
        Optional<TeamMember> result = teamMemberRepository.findById(1L);
        TeamMember team = result.get();
        // insert(c), update(u) 작업 시 호출
        teamMemberRepository.save(team);
    }

    @Test
    public void insertTest() {
        Team existingTeam = teamRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMember team = TeamMember.builder()
                .name("변학도")
                .team(existingTeam)
                .build();

        // insert(c), update(u) 작업 시 호출
        teamMemberRepository.save(team);
    }

    @Test
    public void testQuery() {
        // Team getTeam = teamRepository.findById(1L).get();
        var result = teamMemberRepository.findByMemberAndTeam(1L);
        for (Object[] objects : result) {
            var member = (TeamMember) objects[0];
            var team = (Team) objects[1];
            System.out.println(member + ", " + team);
        }
    }
}
