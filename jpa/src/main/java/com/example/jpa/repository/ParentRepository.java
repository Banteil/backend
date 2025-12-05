package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Parent;

public interface ParentRepository extends JpaRepository<Parent, Long> {

}
