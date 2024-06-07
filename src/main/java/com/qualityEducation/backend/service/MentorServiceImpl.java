package com.qualityEducation.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.entity.CoursesEntity;
import com.qualityEducation.backend.entity.MentorEntity;
import com.qualityEducation.backend.entity.UserEntity;
import com.qualityEducation.backend.model.Course;
import com.qualityEducation.backend.model.Mentor;
import com.qualityEducation.backend.repository.MentorRepo;
import com.qualityEducation.backend.repository.UserRepo;

@Service
public class MentorServiceImpl implements MentorService {

    private final MentorRepo mentorRepo;
    private final UserRepo userRepo;

    @Autowired
    public MentorServiceImpl(MentorRepo mentorRepo, UserRepo userRepo) {
        this.mentorRepo = mentorRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean addMentor(Mentor mentor, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.err.println("File is empty");
            return false;
        }

        try {
            // Save the image
            String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/mentor";
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.copy(file.getInputStream(), filePath);

            // Set the image name in the mentor object
            mentor.setImage(fileName);

            // Find the user and update their mentor status
            UserEntity userEntity = userRepo.findById(mentor.getUserId()).orElse(null);
            if (userEntity == null || userEntity.getIsMentor()) {
                return false; // User not found or already a mentor
            }

            // Create and save the mentor entity
            MentorEntity mentorEntity = new MentorEntity();
            BeanUtils.copyProperties(mentor, mentorEntity);
            mentorEntity.setUser(userEntity); // Set the user in the mentor entity
            mentorRepo.save(mentorEntity);

            // Update the user entity to mark them as a mentor
            userEntity.setIsMentor(true);
            userRepo.save(userEntity);

            System.out.println("Mentor details saved successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Mentor getMentor(Long id) {
        MentorEntity mentorEntity = mentorRepo.findById(id).orElse(null);
        if (mentorEntity != null) {
            Mentor mentor = new Mentor();
            BeanUtils.copyProperties(mentorEntity, mentor);
            if (mentorEntity.getUser() != null) {
                mentor.setUserId(mentorEntity.getUser().getId()); // Set the userId field
            }

            List<Course> courses = new ArrayList<>();
            if (mentorEntity.getCourses() != null) {
                for (CoursesEntity courseEntity : mentorEntity.getCourses()) {
                    Course course = new Course();
                    BeanUtils.copyProperties(courseEntity, course);
                    course.setMentor(mentor);
                    courses.add(course);
                }
            }
            mentor.setCourses(courses);
            return mentor;
        }
        return null;
    }

    @Override
    public List<Mentor> getAllMentor() {
        List<MentorEntity> mentorEntityList = mentorRepo.findAll();
        List<Mentor> mentorList = new ArrayList<>();
        for (MentorEntity mentorEntity : mentorEntityList) {
            Mentor mentor = new Mentor();
            BeanUtils.copyProperties(mentorEntity, mentor);
            if (mentorEntity.getUser() != null) {
                mentor.setUserId(mentorEntity.getUser().getId()); // Set the userId field
            }

            List<Course> courses = new ArrayList<>();
            if (mentorEntity.getCourses() != null) {
                for (CoursesEntity courseEntity : mentorEntity.getCourses()) {
                    Course course = new Course();
                    BeanUtils.copyProperties(courseEntity, course);
                    course.setMentor(mentor);
                    courses.add(course);
                }
            }
            mentor.setCourses(courses);
            mentorList.add(mentor);
        }
        return mentorList;
    }
}
