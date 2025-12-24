package com.example.movietalk.movie.repository;

import static com.example.movietalk.movie.entity.QMovie.movie;
import static com.example.movietalk.movie.entity.QMovieImage.movieImage;
import static com.example.movietalk.movie.entity.QReview.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.entity.Movie;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MovieRepositoryImpl extends QuerydslRepositorySupport implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Movie.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<MovieDTO> getListPageQuerydsl(Pageable pageable) {

        // 1. 데이터 조회 쿼리
        List<MovieDTO> content = queryFactory
                .select(Projections.constructor(MovieDTO.class,
                        movie.mno,
                        movie.title,
                        movieImage, // MovieImage 객체 전달 (DTO 내부에서 변환 처리)
                        review.count(), // reviewCnt
                        review.grade.avg().coalesce(0.0), // avg
                        movie.createDateTime,
                        movie.updateDateTime))
                .from(movie)
                .leftJoin(movieImage).on(movieImage.movie.eq(movie).and(movieImage.ord.eq(0)))
                .leftJoin(review).on(review.movie.eq(movie))
                .groupBy(movie.mno, movieImage) // mImages가 아닌 movieImage 개별 객체 기준
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(movie.mno.desc())
                .fetch();

        // 2. 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(movie.count())
                .from(movie);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}