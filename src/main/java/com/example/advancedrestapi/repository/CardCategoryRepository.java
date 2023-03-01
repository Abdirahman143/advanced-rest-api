package com.example.advancedrestapi.repository;

import com.example.advancedrestapi.model.CardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCategoryRepository extends JpaRepository<CardCategory,Long> {
}
