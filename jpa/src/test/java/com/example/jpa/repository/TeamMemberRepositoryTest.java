package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

@SpringBootTest
public class TeamMemberRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void readTest() {
        TeamMember result = teamMemberRepository.findById(1L).get();
        System.out.println(result.toString());
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
                .name("성춘향")
                .team(existingTeam)
                .build();

        // insert(c), update(u) 작업 시 호출
        teamMemberRepository.save(team);
    }
}
