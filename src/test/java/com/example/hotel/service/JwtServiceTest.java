package com.example.hotel.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private JwtService jwtService;
    @Mock
    private UserDetails userDetails;
    

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        //userDetails = org.mockito.Mockito.mock(UserDetails.class);
    }
    @Test
    public void testGenerateToken(){
        when(userDetails.getUsername()).thenReturn("admin@hotel.com");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length()>50);
    }
    @Test
    public void testextractUsername(){
        when(userDetails.getUsername()).thenReturn("admin@hotel.com");
        String token = jwtService.generateToken(userDetails);
        assertEquals("admin@hotel.com", jwtService.extractUsername(token));
    }
    @Test
    public void testIsTokenValidTrue(){
        when(userDetails.getUsername()).thenReturn("admin@hotel.com");
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
    @Test
    public void testIsTokenValidFalse(){
        when(userDetails.getUsername()).thenReturn("admin@hotel.com");
        String token = jwtService.generateToken(userDetails);
        UserDetails fakeUser = org.mockito.Mockito.mock(UserDetails.class);
        when(fakeUser.getUsername()).thenReturn("fake@hotel.com");
        assertFalse(jwtService.isTokenValid(token, fakeUser));
    }
}