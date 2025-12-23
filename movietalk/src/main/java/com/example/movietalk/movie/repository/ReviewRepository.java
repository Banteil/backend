package com.example.movietalk.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movietalk.movie.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
