package com.example.hotel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.User;
import com.example.hotel.repository.UserRepository;

@Service  
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public String register(com.example.hotel.dto.RegisterRequestDTO request) {

        if (request.getRole() == com.example.hotel.entity.Role.ADMIN) {
            if (userRepository.existsByRole(com.example.hotel.entity.Role.ADMIN)) {
                throw new RuntimeException("Security Alert: An Admin already exists! Cannot register multiple admins.");
            }
        }
        User user = new User();
        
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);
        
        return "User registered successfully!";
}
}

