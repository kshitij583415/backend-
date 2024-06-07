package com.qualityEducation.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public CoursesServiceImpl(CoursesRepo coursesRepo, MentorRepo mentorRepo) {
        this.coursesRepo = coursesRepo;
        this.mentorRepo = mentorRepo;
    }

    @Override
    public boolean addCourse(Course course) {
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
