package com.example.board.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = { "roles" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByEmailAndFromSocial(String email, boolean fromSocial);
}
