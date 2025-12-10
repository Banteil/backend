package com.example.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpa.entity.Item;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface ItemRepository extends JpaRepository<Item, String> {
    // 집계함수 사용
    @Query("select count(i), sum(i.itemPrice), avg(i.itemPrice), max(i.itemPrice), min(i.itemPrice) from Item i")
    List<Object[]> aggr();
}
