package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Board;

@SpringBootTest
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void readTest() {
        Board result = boardRepository.findById(1L).get();
        System.out.println(result.toString());
    }

    @Test
    public void readAllTest() {
        List<Board> results = boardRepository.findAll();
        results.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    @Test
    public void deleteTest() {
        // studentRepository.delete(null);
        boardRepository.deleteById(1L);
    }

    @Test
    public void updateTest() {
        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.get();

        board.setTitle("스카이림");
        board.setContent("최후의 갓겜");

        // insert(c), update(u) 작업 시 호출
        boardRepository.save(board);
    }

    @Test
    public void insertTest() {
        Board board = Board.builder()
                .title("스타필드")
                .content("토드 하워드의 사업 철칙이 담긴 책")
                .writer("토도키 하와도")
                .build();

        // insert(c), update(u) 작업 시 호출
        boardRepository.save(board);
    }

    @Test
    public void insertArrayTest() {
        List<Board> boardList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("스타필드 " + i + "권")
                    .content("토드 하워드의 사상이 담긴 책")
                    .writer("토도키 하와도")
                    .build();
            boardList.add(board);
        }

        boardRepository.saveAll(boardList);
    }

    @Test
    public void findBy() {
        // var find =
        // boardRepository.findByTitleContainingAndIdGreaterThanOrderByIdDesc("필드",
        // 50L);
        // for (Board board : find) {
        // System.out.println(board);
        // }

        var find = boardRepository.findByTitleAndId2("스타", 20L);
        for (Board board : find) {
            System.out.println(board);
        }
    }
}
