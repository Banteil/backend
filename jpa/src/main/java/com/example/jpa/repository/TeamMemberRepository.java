package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;
import java.util.List;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);

    @Query("select m, t from TeamMember m join m.team t where t = :team")
    List<Object[]> findByMemberAndTeam(Team team);

    @Query("select m, t from TeamMember m join m.team t where t.id = :id")
    List<Object[]> findByMemberAndTeam(Long id);

    @Query("select m, t from TeamMember m left join m.team t")
    List<Object[]> findByMemberAndTeam();
}
