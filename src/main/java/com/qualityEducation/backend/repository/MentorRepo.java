package com.qualityEducation.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualityEducation.backend.entity.MentorEntity;

@Repository
public interface MentorRepo extends JpaRepository<MentorEntity, Long> {
}
