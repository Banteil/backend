package com.example.memo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.memo.dto.MemoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(value = AuditingEntityListener.class)
@Entity // == 이 클래스는 테이블과 연동되어 있음
@Table(name = "memotbl")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Memo {
    // 컬럼 : mno, memo_text, create_date, update_date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mno")
    private Long id;

    @Column(nullable = false, name = "memo_text")
    private String text;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    public MemoDTO toDTO() {
        return MemoDTO.builder()
                .id(this.id)
                .text(this.text)
                .createDateTime(this.createDateTime)
                .updateDateTime(this.updateDateTime)
                .build();
    }
}
