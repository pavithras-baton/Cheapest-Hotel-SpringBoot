package com.example.hotel.service;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import  org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.hotel.dto.AuthResponseDTO;
import com.example.hotel.dto.LoginRequestDTO;
import com.example.hotel.dto.RegisterRequestDTO;
import com.example.hotel.entity.Role;
import com.example.hotel.entity.User;
import com.example.hotel.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequestDTO();
        loginRequest = new LoginRequestDTO();
        mockUser = new User();

        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.USER);

        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(Role.USER);
    }
    @Test
    public void testRegisterSuccessforUser(){
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        
        String result = authService.register(registerRequest);
        assertEquals("User registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    public void testRegisterAdminSuccess(){
        registerRequest.setRole(Role.ADMIN);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");  
        when(userRepository.existsByRole(Role.ADMIN)).thenReturn(false);
        String result = authService.register(registerRequest);
        assertEquals("Admin registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    public void testRegisterAdminFailure(){
        registerRequest.setRole(Role.ADMIN);
        when(userRepository.existsByRole(Role.ADMIN)).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("Security Alert: An Admin already exists! Cannot register multiple admins.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    public void testLoginSuccess(){
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("Fake_JWT_Token");
        
        AuthResponseDTO response = authService.login(loginRequest); 
        
        assertNotNull(response);
        assertEquals("Login successful!", response.getMessage());
        assertEquals("Fake_JWT_Token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));        
    }
    @Test
    public void testLoginUserNotFound(){
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            authService.login(loginRequest);

        });
        assertEquals("User not found", exception.getMessage());

    }
}
