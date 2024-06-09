package com.qualityEducation.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.entity.CoursesEntity;
import com.qualityEducation.backend.entity.MentorEntity;
import com.qualityEducation.backend.model.Course;
import com.qualityEducation.backend.model.Mentor;
import com.qualityEducation.backend.repository.CoursesRepo;
import com.qualityEducation.backend.repository.MentorRepo;

@Service
public class CoursesServiceImpl implements CoursesServices {

    private final CoursesRepo coursesRepo;
    private final MentorRepo mentorRepo;
    private final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/course/";
    private final String baseURL = "http://localhost:9090/images/course/";

    @Autowired
    public CoursesServiceImpl(CoursesRepo coursesRepo, MentorRepo mentorRepo) {
        this.coursesRepo = coursesRepo;
        this.mentorRepo = mentorRepo;
    }

    @Override
    public boolean addCourse(MultipartFile file, Course course) {
        CoursesEntity coursesEntity = new CoursesEntity();
        BeanUtils.copyProperties(course, coursesEntity);

        // Check if mentorId exists
        if (course.getMentor() != null && course.getMentor().getId() != null) {
            Optional<MentorEntity> optionalMentorEntity = mentorRepo.findById(course.getMentor().getId());
            if (optionalMentorEntity.isPresent()) {
                MentorEntity mentorEntity = optionalMentorEntity.get();
                coursesEntity.setMentor(mentorEntity);
            } else {
                // Handle case where mentor does not exist
                return false;
            }
        }

        // Set the image URL if file is not null and not empty
        if (file != null && !file.isEmpty()) {
            try {
                System.err.println("Upload Directory: " + uploadDirectory);
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDirectory, fileName);
                Files.copy(file.getInputStream(), filePath);
                String fileURL = baseURL + fileName; // Construct the file URL
                coursesEntity.setImage(fileURL);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            coursesRepo.save(coursesEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<CoursesEntity> coursesEntityList = coursesRepo.findAll();
        List<Course> courseList = new ArrayList<>();

        for (CoursesEntity coursesEntity : coursesEntityList) {
            Course course = new Course();
            BeanUtils.copyProperties(coursesEntity, course);

            if (coursesEntity.getMentor() != null) {
                Mentor mentor = new Mentor();
                BeanUtils.copyProperties(coursesEntity.getMentor(), mentor);
                course.setMentor(mentor);
            }

            courseList.add(course);
        }
        return courseList;
    }
}
