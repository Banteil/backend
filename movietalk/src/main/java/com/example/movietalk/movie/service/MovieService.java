package com.example.movietalk.movie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.common.dto.PageRequestDTO;
import com.example.movietalk.common.dto.PageResultDTO;
import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@Setter
@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public PageResultDTO<MovieDTO> getMovieList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("mno").descending());

        Page<MovieDTO> result = movieRepository.getListPageQuerydsl(pageable);

        return PageResultDTO.<MovieDTO>withAll()
                .dtoList(result.getContent())
                .totalCount(result.getTotalElements())
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public void getMovie(Long mno) {
        movieRepository.getMovieWithAll(mno);
    }
}
