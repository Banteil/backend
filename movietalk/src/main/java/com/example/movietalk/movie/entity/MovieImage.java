package com.example.movietalk.movie.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.movietalk.common.entity.BaseEntity;
import com.example.movietalk.movie.dto.MovieImageDTO;

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
@Table(name = "movie_image_tbl")
@Builder
@Getter
@Setter
@ToString(exclude = { "movie" })
@NoArgsConstructor
@AllArgsConstructor
public class MovieImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String imgName;

    private int ord; // 이미지 순서

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mno")
    private Movie movie;

    public static MovieImage from(MovieImageDTO dto, Movie movie) {
        return MovieImage.builder()
                .inum(dto.getInum())
                .uuid(dto.getUuid())
                .path(dto.getPath())
                .imgName(dto.getImgName())
                .ord(dto.getOrd())
                .movie(movie)
                .build();
    }
}
