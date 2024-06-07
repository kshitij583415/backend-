package com.qualityEducation.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualityEducation.backend.entity.BooksEntity;

@Repository
public interface BooksRepo extends JpaRepository<BooksEntity, Long> {
}
