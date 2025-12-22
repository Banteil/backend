package com.example.board.member.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.board.member.entity.constant.MemberRole;
import com.example.board.post.entity.BaseEntity;
import com.example.board.post.entity.Board;
import com.example.board.reply.entity.Reply;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(value = AuditingEntityListener.class)
@Entity
@Table(name = "board_member")
@Builder
@Getter
@Setter
@ToString(exclude = "boards")
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "board_member_roles", joinColumns = @JoinColumn(name = "member_email"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public void addMemberRole(MemberRole role) {
        roles.add(role);
    }
}
