package com.qualityEducation.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qualityEducation.backend.entity.UserEntity;
import com.qualityEducation.backend.model.User;
import com.qualityEducation.backend.repository.UserRepo;
import com.qualityEducation.backend.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    @Value("${jwt.secret}")
    private String jwtSecret;
    private JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public void addUser(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        // Hash the password before saving
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        userEntity.setPassword(hashedPassword);

        userRepo.save(userEntity);
    }

    @Override
    public User getUser(Long id) {
        UserEntity userEntity = userRepo.findById(id).orElse(null);
        if (userEntity != null) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            // Set isMentor field in the User object
            user.setIsMentor(userEntity.getIsMentor());
            return user;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> userEntityList = userRepo.findAll();
        List<User> userList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public boolean makeUserMentor(Long id) {
        UserEntity userEntity = userRepo.findById(id).orElse(null);
        if (userEntity != null && !userEntity.getIsMentor()) {
            userEntity.setIsMentor(true);
            userRepo.save(userEntity);
            return true;
        }
        return false;
    }
    @Override
    public String authenticateUser(String email, String password, HttpServletResponse response) {
        UserEntity userEntity = userRepo.findByEmail(email);
        if (userEntity != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = userEntity.getPassword();

            // Debug statements to check passwords
            System.out.println("User found: " + userEntity.getEmail());
            System.out.println("Entered password: " + password);
            System.out.println("Stored hashed password: " + hashedPassword);

            if (encoder.matches(password, userEntity.getPassword())) {
                Long userId = userEntity.getId();
                List<String> roles = new ArrayList<>();
                if (userEntity.getIsAdmin()) {
                    roles.add("ROLE_ADMIN");
                }
                if (userEntity.getIsMentor()) {
                    roles.add("ROLE_MENTOR");
                }
                String token = jwtUtil.generateToken(userId, roles);
                System.out.println("Generated JWT Token: " + token);
                jwtUtil.addTokenToCookie(token, response);
                return token;
            } else {
                System.out.println("Password mismatch");
            }
        } else {
            System.out.println("User not found with email: " + email);
        }
        return null;
    }
}
