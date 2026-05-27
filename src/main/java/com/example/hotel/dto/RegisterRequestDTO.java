package com.example.hotel.dto;
import com.example.hotel.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class RegisterRequestDTO {
    private String email;
    private String password;
    private Role role;
}
