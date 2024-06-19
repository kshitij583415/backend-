package com.qualityEducation.backend.service;

import java.util.List;

import com.qualityEducation.backend.model.User;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void addUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    boolean makeUserMentor(Long id);
    String authenticateUser(String email, String password, HttpServletResponse response);
}
