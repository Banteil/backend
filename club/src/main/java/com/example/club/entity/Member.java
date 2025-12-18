package com.example.club.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.club.entity.constant.ClubMemberRole;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "club_member")
@Entity
public class Member {
    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<ClubMemberRole> roles = new HashSet<>();

    public void addMemberRole(ClubMemberRole role) {
        roles.add(role);
    }
}
