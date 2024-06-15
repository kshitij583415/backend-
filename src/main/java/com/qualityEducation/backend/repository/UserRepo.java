package com.qualityEducation.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualityEducation.backend.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
