package com.qualityEducation.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Mentor;

public interface MentorService {
    boolean addMentor(Mentor mentor, MultipartFile file);

    Mentor getMentor(Long id);

    List<Mentor> getAllMentor();
}
