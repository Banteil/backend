package com.example.movietalk.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movietalk.movie.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

}