package com.example.movietalk.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.entity.constant.Role;
import com.example.movietalk.member.repository.MemberRepositoy;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieImageRepository;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository imageRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepositoy memberRepositoy;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void memberInsertTest() {
        List<Member> members = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .nickname("user" + i)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            member.setRole(Role.MEMBER);
            members.add(member);
        });
        memberRepositoy.saveAll(members);
    }

    @Test
    public void reviewInsertTest() {
        List<Review> reviews = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            long mno = (int) (Math.random() * 100) + 1;
            long mid = (int) (Math.random() * 10) + 1;
            Review review = Review.builder()
                    .member(memberRepositoy.findById(mid).get())
                    .movie(movieRepository.findById(mno).get())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("review...." + i)
                    .build();
            reviews.add(review);
        });
        reviewRepository.saveAll(reviews);
    }

    @Test
    public void insertTest() {
        List<Movie> movies = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("Movie Title " + i)
                    .build();

            int count = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < count; j++) {
                MovieImage mI = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .path("localhost:8080/assets/img/")
                        .ord(j)
                        .imgName("test " + j + ".jpg")
                        .build();
                movie.getImages().add(mI);
            }
            movies.add(movie);
        });
        movieRepository.saveAll(movies);
    }

    // 조회
    // mno, 영화이미지중첫번째이름, 영화제목, 리뷰수, 리뷰평균점수, 영화등록일
    @Test
    @Transactional
    public void movieListTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
        System.out.println("Size : " + result.getSize());
    }

    @Test
    public void getMovieWithAllTest() {
        Object[] result = movieRepository.getMovieWithAll(1L);
        System.out.println(Arrays.toString(result));
    }
}
