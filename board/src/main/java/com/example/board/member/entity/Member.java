package com.example.board.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.board.post.entity.BaseEntity;
import com.example.board.post.entity.Board;
import com.example.board.reply.entity.Reply;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
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

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();
}
