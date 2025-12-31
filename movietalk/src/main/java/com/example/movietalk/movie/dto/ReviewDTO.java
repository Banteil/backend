package com.example.movietalk.movie.dto;

import java.time.LocalDateTime;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long rno;
    private Long mno;
    private Long mid;
    private String nickname;
    private String email;

    private int grade;
    private String text;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Review toEntity() {
        return Review.builder()
                .rno(rno)
                .movie(Movie.builder().mno(mno).build())
                .member(Member.builder().mid(mid).build())
                .grade(grade)
                .text(text)
                .build();
    }
}
