package com.qualityEducation.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Mentor;
import com.qualityEducation.backend.service.MentorService;
import com.qualityEducation.backend.service.UserService;

@RestController
public class MentorController {

    private final MentorService mentorService;
    private final UserService userService;

    @Autowired
    public MentorController(MentorService mentorService, UserService userService) {
        this.mentorService = mentorService;
        this.userService = userService;
    }

    @PostMapping("/createMentor")
    public ResponseEntity<String> createMentor(@RequestParam("file") MultipartFile file,
            @ModelAttribute Mentor mentor) {
        if (file.isEmpty()) {
            return ResponseEntity.status(400).body("Image not found");
        }
        try {
            boolean isMentorCreated = mentorService.addMentor(mentor, file);
            if (isMentorCreated) {
                return ResponseEntity.status(200).body("Mentor created");
            }
            return ResponseEntity.status(500).body("Not able to create mentor");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/getMentor/{id}")
    public ResponseEntity<Mentor> getMentor(@PathVariable("id") Long mentorId) {
        try {
            Mentor mentor = mentorService.getMentor(mentorId);
            if (mentor != null) {
                return ResponseEntity.status(200).body(mentor);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getAllMentor")
    public ResponseEntity<List<Mentor>> getAllMentor() {
        try {
            List<Mentor> mentorList = mentorService.getAllMentor();
            if (!mentorList.isEmpty()) {
                return ResponseEntity.status(200).body(mentorList);
            }
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
