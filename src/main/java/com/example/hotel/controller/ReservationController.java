package com.example.hotel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.dto.HotelResponseDTO;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;
import com.example.hotel.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService ReservationService;
    public ReservationController(ReservationService ReservationService) {
        this.ReservationService = ReservationService;
    }
    @PostMapping()
    public ReservationResponseDTO bookHotel(@RequestBody ReservationRequestDTO request) {
        return ReservationService.makeReservation(request);

    }
    @GetMapping()
    public List<HotelResponseDTO> getAvailableHotels() {
        return ReservationService.getAllHotels();
    }
}