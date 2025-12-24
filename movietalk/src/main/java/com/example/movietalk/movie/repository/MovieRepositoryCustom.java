package com.example.movietalk.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.movietalk.movie.dto.MovieDTO;

public interface MovieRepositoryCustom {
    Page<MovieDTO> getListPageQuerydsl(Pageable pageable);
}