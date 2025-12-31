package com.example.movietalk.movie.controller;

import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 특정 영화의 모든 리뷰 반환
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno) {
        log.info("--------------list---------------");
        log.info("MNO: " + mno);
        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    // 리뷰 등록
    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO) {
        log.info("--------------add MovieReview---------------");
        log.info("reviewDTO: " + movieReviewDTO);
        Long rno = reviewService.register(movieReviewDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/{mno}/{rno}")
    public ResponseEntity<Long> removeReview(
            @PathVariable("mno") Long mno, // 이름을 명시하세요
            @PathVariable("rno") Long rno) { // 이름을 명시하세요
        log.info("---------------remove MovieReview----------------");
        log.info("mno: " + mno + ", rno: " + rno);
        reviewService.remove(rno);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @PutMapping("/{mno}/{rno}")
    public ResponseEntity<Long> modifyReview(
            @PathVariable("mno") Long mno,
            @PathVariable("rno") Long rno,
            @RequestBody ReviewDTO movieReviewDTO) {
        log.info("---------------modify MovieReview----------------");
        movieReviewDTO.setRno(rno);
        reviewService.modify(movieReviewDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }
}