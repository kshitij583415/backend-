package com.qualityEducation.backend.service;

import java.util.List;

import com.qualityEducation.backend.model.Course;

public interface CoursesServices {
    public boolean addCourse(Course course);

    public List<Course> getAllCourses();
}
