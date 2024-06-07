package com.qualityEducation.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qualityEducation.backend.model.User;
import com.qualityEducation.backend.service.UserService;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/addUsers")
    public ResponseEntity<String> addUsers(@RequestBody User user) {
        try {
            userService.addUser(user);
            return ResponseEntity.status(201).body("User added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add user");
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (!users.isEmpty()) {
                return ResponseEntity.ok(users);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/makeUserMentor/{id}")
    public ResponseEntity<String> makeUserMentor(@PathVariable Long id) {
        try {
            boolean isUserUpdated = userService.makeUserMentor(id);
            if (isUserUpdated) {
                return ResponseEntity.status(200).body("User is now a mentor");
            }
            return ResponseEntity.status(400).body("User not found or already a mentor");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
