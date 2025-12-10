package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Board;
import java.util.List;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitle(String title);

    List<Board> findByContent(String content);

    List<Board> findByTitleEndingWith(String title);

    // id > 0 order by id desc
    List<Board> findByTitleContainingAndIdGreaterThanOrderByIdDesc(String title, Long id);

    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    @Query("select b from Board b where b.title = ?1")
    List<Board> findByTitle2(String title);

    @Query(value = "SELECT * FROM boardtbl b WHERE b.title LIKE CONCAT('%', :title, '%') AND b.id > :id ORDER BY b.id DESC", nativeQuery = true)
    List<Board> findByTitleAndId2(@Param("title") String title, @Param("id") Long id);
}
