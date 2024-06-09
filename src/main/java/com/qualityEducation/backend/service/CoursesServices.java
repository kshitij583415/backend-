package com.qualityEducation.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Course;

public interface CoursesServices {
    public boolean addCourse(MultipartFile file, Course course);

    public List<Course> getAllCourses();
}
