package com.example.movietalk.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.movietalk.movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = "select m, mi, count(distinct r), avg(coalesce(r.grade,0)) from Movie m left outer join MovieImage mi on mi.movie = m and mi.ord = 0 left outer join Review r on r.movie = m group by m, mi", countQuery = "select count(m) from Movie m")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select m, count(distinct r), avg(coalesce(r.grade,0)) from Movie m left outer join Review r on r.movie = m where m.mno = :mno group by m")
    Object[] getMovieWithAll(@Param("mno") Long mno);
}
