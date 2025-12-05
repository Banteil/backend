package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Child;

public interface ChildRepository extends JpaRepository<Child, Long> {

}
