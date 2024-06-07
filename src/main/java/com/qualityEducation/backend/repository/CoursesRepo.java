package com.qualityEducation.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualityEducation.backend.entity.CoursesEntity;

public interface CoursesRepo extends JpaRepository<CoursesEntity, Long> {

}
