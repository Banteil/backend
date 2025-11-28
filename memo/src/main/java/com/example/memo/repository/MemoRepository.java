package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memo.entity.Memo;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface MemoRepository extends JpaRepository<Memo, Long> {

    
} 
