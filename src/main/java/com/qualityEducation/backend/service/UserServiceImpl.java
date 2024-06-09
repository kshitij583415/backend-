package com.qualityEducation.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qualityEducation.backend.entity.UserEntity;
import com.qualityEducation.backend.model.User;
import com.qualityEducation.backend.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
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
}
