package com.example.hotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.dto.HotelRequestDTO;
import com.example.hotel.service.HotelService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final HotelService hotelService;

    public AdminController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/hotels")
    public ResponseEntity<String> addHotel(@RequestBody HotelRequestDTO request) {
        String response = hotelService.addHotel(request);
        return ResponseEntity.ok(response);
    }
}