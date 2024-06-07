package com.qualityEducation.backend.service;

import java.util.List;

import com.qualityEducation.backend.model.User;

public interface UserService {
    void addUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    boolean makeUserMentor(Long id);
}
