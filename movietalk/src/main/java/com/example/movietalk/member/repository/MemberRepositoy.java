package com.example.movietalk.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movietalk.member.entity.Member;

public interface MemberRepositoy extends JpaRepository<Member, Long> {

}
