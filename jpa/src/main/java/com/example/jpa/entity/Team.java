package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teamtbl")
@Entity
public class Team {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "team_id")
    private Long id;
    @Column(nullable = false)
    private String name;
}
