package com.example.club.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.club.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    // @Query("select m from Member m left join fetch m.roles where m.email = :email
    // and m.fromSocial = :fromSocial")
    @EntityGraph(attributePaths = { "roles" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByEmailAndFromSocial(String email, boolean fromSocial);
}
