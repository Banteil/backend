package com.example.movietalk.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long mno;
    private String title;
    @Builder.Default
    private List<MovieImageDTO> mImages = new ArrayList<>();

    private double avg;
    private Long reviewCnt;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MovieDTO(Long mno, String title, MovieImage movieImage, Long reviewCnt, Double avg, LocalDateTime regDate,
            LocalDateTime modDate) {
        this.mno = mno;
        this.title = title;
        if (movieImage != null) {
            this.mImages = List.of(MovieImageDTO.from(movieImage));
        } else {
            this.mImages = new ArrayList<>();
        }
        this.reviewCnt = reviewCnt != null ? reviewCnt : 0L;
        this.avg = avg != null ? avg : 0.0;
        this.createDate = regDate;
        this.updateDate = modDate;
    }

    public static MovieDTO from(Movie movie) {
        // 1. 기본 필드 매핑 및 Builder 생성
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .createDate(movie.getCreateDateTime())
                .updateDate(movie.getUpdateDateTime())
                .build();

        if (movie.getImages() != null) {
            List<MovieImageDTO> movieImageDTOList = movie.getImages().stream()
                    .map(MovieImageDTO::from)
                    .collect(Collectors.toList());

            movieDTO.setMImages(movieImageDTOList);
        }
        return movieDTO;
    }
}
