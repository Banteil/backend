package com.example.book.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.book.dto.BookDTO;

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
@Table(name = "booktbl")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String author;

    @Column
    private String description;

    @CreationTimestamp // insert 시 자동으로 일자 삽입
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    public BookDTO toDTO() {
        return BookDTO.builder()
                .id(this.id)
                .isbn(this.isbn)
                .title(this.title)
                .price(this.price)
                .author(this.author)
                .createDateTime(this.createDateTime)
                .updateDateTime(this.updateDateTime)
                .build();
    }
}
