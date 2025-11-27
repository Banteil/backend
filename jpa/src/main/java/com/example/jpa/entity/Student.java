package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@EntityListeners(value = AuditingEntityListener.class)
@Builder
@Table(name = "stutbl")
@Entity // == 이 클래스는 테이블과 연동되어 있음
public class Student {
    // @SequenceGenerator(name = "stu_seq_gen", sequenceName = "stu_seq", allocationSize = 1)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stu_seq_gen")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name="sname", length = 50, nullable = false, unique = true)
    @Column(columnDefinition = "varchar(50) not null unique")
    private String name;

    @Column
    private String addr;

    @Column(columnDefinition = "varchar(1) CONSTRAINT chk_gender CHECK (gender IN ('M', 'F'))")
    private String gender;

    @CreationTimestamp // insert 시 자동으로 일자 삽입
    private LocalDateTime createDateTime1;

    @CreatedDate // spring boot 설정 후 삽입
    private LocalDateTime createDateTime2;
}
