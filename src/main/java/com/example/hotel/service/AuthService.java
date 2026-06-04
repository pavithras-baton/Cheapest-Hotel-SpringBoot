package com.example.hotel.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hotel.dto.AuthResponseDTO;
import com.example.hotel.dto.LoginRequestDTO;
import com.example.hotel.entity.User;
import com.example.hotel.repository.UserRepository;

@Service  
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
        if (request.getRole() == com.example.hotel.entity.Role.ADMIN) {
            return "Admin registered successfully";
        }
        
        return "User registered successfully";
}

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        AuthResponseDTO response = new AuthResponseDTO();
        response.setMessage("Login successful!");
        response.setToken(token);
        return response;
    }

}

