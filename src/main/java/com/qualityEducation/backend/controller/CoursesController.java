package com.qualityEducation.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Course;
import com.qualityEducation.backend.service.CoursesServices;

@RestController
public class CoursesController {

    @Autowired
    CoursesServices coursesServices;

    @PostMapping("/addCourses")
    public ResponseEntity<String> addCourses(@RequestParam("file") MultipartFile file, @ModelAttribute Course course) {
        try {
            if (coursesServices.addCourse(file, course))
                return ResponseEntity.status(201).body("Course added successfully");
            return ResponseEntity.status(404).body("Course not added");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add Course");
        }
    }

    @GetMapping("/getAllCourses")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = coursesServices.getAllCourses();
            if (!courses.isEmpty())
                return ResponseEntity.ok(courses);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
