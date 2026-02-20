package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void init() {
        userRepository.createTable();
    }

    public UserEntity create(String name) {
        UserEntity user = new UserEntity(name);
        userRepository.save(user);
        return user;
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity getOne(Long id) {
        return userRepository.findById(id);
    }

    public void update(Long id, String name) {
        UserEntity user = new UserEntity(name);
        user.setId(id);
        userRepository.update(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}