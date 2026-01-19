package com.example.ecopulse.service;

import com.example.ecopulse.dto.UserResponse;
import com.example.ecopulse.dto.UserSignupRequest;
import com.example.ecopulse.entity.User;
import com.example.ecopulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signup(UserSignupRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email is already registered");
        });

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(request.getPassword())  // will hash later
                .name(request.getName())
                .homeLocation(request.getHomeLocation())
                .build();

        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .homeLocation(user.getHomeLocation())
                .build();
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .homeLocation(user.getHomeLocation())
                .build();
    }
}