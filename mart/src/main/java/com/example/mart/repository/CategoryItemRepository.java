package com.example.mart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mart.entity.Category;
import com.example.mart.entity.CategoryItem;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
    List<CategoryItem> findByCategoryId(Long categoryId);

    List<CategoryItem> findByCategory(Category category);
}
