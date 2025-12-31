package com.example.movietalk.movie.service;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.repository.MemberRepository;
import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository; // 이메일로 mid를 찾기 위해 필요

    public List<ReviewDTO> getListOfMovie(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);

        // 엔티티의 메서드를 사용하여 변환
        return result.stream().map(Review::toDTO).collect(Collectors.toList());
    }

    public Long register(ReviewDTO reviewDTO) {
        Member member = memberRepository.findByEmail(reviewDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        reviewDTO.setMid(member.getMid());

        // DTO의 메서드를 사용하여 변환
        Review review = reviewDTO.toEntity();
        reviewRepository.save(review);

        return review.getRno();
    }

    @Transactional
    public void modify(ReviewDTO movieReviewDTO) {
        // 기존 리뷰가 존재하는지 먼저 확인
        Review review = reviewRepository.findById(movieReviewDTO.getRno())
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다."));

        // 평점과 텍스트 내용 업데이트
        review.setGrade(movieReviewDTO.getGrade());
        review.setText(movieReviewDTO.getText());

        // Dirty Checking(변경 감지)에 의해 트랜잭션 종료 시 자동 반영됩니다.
        reviewRepository.save(review);
    }

    // 리뷰 삭제
    @Transactional
    public void remove(Long rno) {
        reviewRepository.deleteById(rno);
    }
}