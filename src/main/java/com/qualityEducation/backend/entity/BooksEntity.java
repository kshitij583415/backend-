package com.qualityEducation.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class BooksEntity {
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
