package com.example.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Team;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t join fetch t.members where t.id = :id")
    Optional<Team> findByIdWithMembers(@Param("id") Long id);
}
