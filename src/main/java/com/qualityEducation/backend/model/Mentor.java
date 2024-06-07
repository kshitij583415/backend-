package com.qualityEducation.backend.model;

import java.util.List;

import lombok.Data;

@Data
public class Mentor {
    private Long id;
    private String about;
    private String certification;
    private Integer totalCourses;
    private Integer rating;
    private Integer exp;
    private Boolean graduated;
    private String language;
    private String image;
    private List<Course> courses;
    private Long userId; // Add this line
}
