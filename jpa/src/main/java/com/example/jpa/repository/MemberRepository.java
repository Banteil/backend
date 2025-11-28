package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Member;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface MemberRepository extends JpaRepository<Member, Long> {

    
} 
