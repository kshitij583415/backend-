package com.qualityEducation.backend.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 0, max = 30)
    private String title;
    @Size(min = 0, max = 5)
    private Integer rating;
    private String image;
    private boolean isPopular = false;
    private boolean isNew = false;
}
