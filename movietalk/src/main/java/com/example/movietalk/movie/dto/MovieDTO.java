package com.example.movietalk.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    // public static MovieDTO from(Movie movie, List<MovieImage> mImages, Long
    // reviewCnt, Double avg) {
    // List<MovieImageDTO> movieImageDTOList = mImages.stream()
    // .map(MovieImageDTO::from)
    // .collect(Collectors.toList());

    // MovieDTO movieDTO = MovieDTO.builder()
    // .mno(movie.getMno())
    // .title(movie.getTitle())
    // .avg(avg != null ? avg : 0.0)
    // .reviewCnt(reviewCnt)
    // .createDate(movie.getCreateDateTime())
    // .updateDate(movie.getUpdateDateTime())
    // .build();
    // movieDTO.setMImages(movieImageDTOList);
    // return movieDTO;
    // }
}
