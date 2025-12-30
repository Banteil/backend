package com.example.movietalk.movie.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.movietalk.common.entity.BaseEntity;
import com.example.movietalk.movie.dto.MovieDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "movie_tbl")
@Builder
@Getter
@Setter
@ToString(exclude = { "images" })
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public static Movie from(MovieDTO movieDto) {
        Movie movie = Movie.builder()
                .mno(movieDto.getMno())
                .title(movieDto.getTitle())
                .build();

        if (movieDto.getMImages() != null && !movieDto.getMImages().isEmpty()) {
            List<MovieImage> movieImageList = movieDto.getMImages().stream()
                    .map(imgDto -> MovieImage.from(imgDto, movie)) // MovieImage의 from 호출
                    .collect(Collectors.toList());

            movie.setImages(movieImageList);
        }

        return movie;
    }

    public void addImage(MovieImage image) {
        this.images.add(image);
    }
}
