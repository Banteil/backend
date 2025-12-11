package com.example.board.reply.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.board.post.entity.BaseEntity;
import com.example.board.post.entity.Board;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(value = AuditingEntityListener.class)
@Entity
@Table(name = "replytbl")
@Builder
@Getter
@Setter
@ToString(exclude = "board")
@NoArgsConstructor
@AllArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    @Column(nullable = false)
    private String text;

    private String replayer;

    // Board와 1:N 관계
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "bno")
    private Board board;
}
