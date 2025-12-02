package com.example.student.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.student.dto.StudentDTO;
import com.example.student.entity.constant.Grade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "stutbl")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column
    private String addr;

    @Column(columnDefinition = "varchar(1) CONSTRAINT chk_gender CHECK (gender IN ('M', 'F'))")
    private String gender;

    // grade => FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    @Enumerated(EnumType.STRING)
    @Column
    private Grade grade;

    @CreationTimestamp // insert 시 자동으로 일자 삽입
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    public StudentDTO toDTO() {
        return StudentDTO.builder()
                .id(this.id)
                .name(this.name)
                .addr(this.addr)
                .gender(this.gender)
                .grade(this.grade)
                .createDateTime(this.createDateTime)
                .updateDateTime(this.updateDateTime)
                .build();
    }
}
