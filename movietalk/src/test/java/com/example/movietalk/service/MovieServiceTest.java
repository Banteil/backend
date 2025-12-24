package com.example.movietalk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.common.dto.PageRequestDTO;
import com.example.movietalk.common.dto.PageResultDTO;
import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.service.MovieService;

@SpringBootTest
public class MovieServiceTest {
    @Autowired
    private MovieService movieService;

    @Test
    @Transactional(readOnly = true)
    public void getMovieListTest() {
        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResultDTO<MovieDTO> result = movieService.getMovieList(requestDTO);
        result.getDtoList().forEach(dto -> System.out.println(dto));
    }
}
