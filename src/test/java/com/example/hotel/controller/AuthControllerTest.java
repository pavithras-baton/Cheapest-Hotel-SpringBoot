package com.example.hotel.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;   
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.hotel.dto.AuthResponseDTO;
import com.example.hotel.dto.LoginRequestDTO;
import com.example.hotel.dto.RegisterRequestDTO;
import com.example.hotel.entity.Role;
import com.example.hotel.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    public MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @MockitoBean

    private AuthService authService;
    @MockitoBean
    private com.example.hotel.service.JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;
    
    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private AuthResponseDTO mockAuthResponse;
    @BeforeEach
    public void setUp() {
        loginRequest= new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        mockAuthResponse = new AuthResponseDTO();
        mockAuthResponse.setMessage("Login successful!");
        mockAuthResponse.setToken("mock-jwt-token");

        registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("admin@hotel.com");
        registerRequest.setPassword("securePass");
        registerRequest.setRole(Role.ADMIN);
    }
    @Test
    public void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(mockAuthResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login successful!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mock-jwt-token"));
    
}
    @Test
    public void testRegisterAdminSuccess() throws Exception {
        registerRequest.setRole(Role.ADMIN);

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn("Admin registered successfully");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Admin registered successfully"));
    }
    @Test
    public void testRegisterSuccess() throws Exception {
        
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn("User registered successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully!"));
    }

    @Test
    public void testRegisterAdminFailure() throws Exception {
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new RuntimeException("Security Alert: An Admin already exists! Cannot register multiple admins."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) 
                .andExpect(MockMvcResultMatchers.content().string("Security Alert: An Admin already exists! Cannot register multiple admins."));
    }
}