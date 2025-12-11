package com.example.board.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

}
