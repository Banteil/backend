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

        List<MovieDTO> content = queryFactory
                .select(Projections.constructor(MovieDTO.class,
                        movie.mno,
                        movie.title,
                        movieImage, // 혹은 개별 필드로 분리 가능하나, groupBy 수정을 우선 시도
                        review.count(),
                        review.grade.avg().coalesce(0.0),
                        movie.createDateTime,
                        movie.updateDateTime))
                .from(movie)
                // 1. 대표 이미지(ord=0)만 조인하도록 고정
                .leftJoin(movieImage).on(movieImage.movie.eq(movie).and(movieImage.ord.eq(0)))
                .leftJoin(review).on(review.movie.eq(movie))
                .groupBy(movie.mno, movieImage.inum)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(movie.mno.desc())
                .fetch();

        // 3. 카운트 쿼리에서도 중복을 제거한 mno 개수를 세어야 합니다.
        JPAQuery<Long> countQuery = queryFactory
                .select(movie.mno.countDistinct())
                .from(movie);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public MovieDTO getMovieDetail(Long mno) {
        // 1. 영화 기본 정보 및 통계 조회
        Movie movieEntity = queryFactory
                .selectFrom(movie)
                .leftJoin(movie.images, movieImage).fetchJoin() // 모든 이미지 페치 조인
                .where(movie.mno.eq(mno))
                .fetchOne();

        // 2. 리뷰 평점 및 개수 별도 계산 (혹은 서비스 레이어에서 처리)
        // 기존에 작성한 MovieDTO 생성자나 Builder를 사용하여 Entity -> DTO 변환
        return MovieDTO.from(movieEntity); // Movie 엔티티의 images 리스트가 DTO의 mImages로 변환됨
    }
}