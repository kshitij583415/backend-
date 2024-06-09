package com.qualityEducation.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class Course {
    private Long id;

    private String courseName;

    private String courseDescription;

    private Integer totalVideos;

    private Integer rating;

    private String standard;
    private String image;

    @JsonIgnoreProperties("courses")
    private Mentor mentor;

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
