package com.example.hotel.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.Service.ReservationService;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService ReservationService;
    public ReservationController(ReservationService ReservationService) {
        this.ReservationService = ReservationService;
    }
    @PostMapping("/book")
    public ReservationResponseDTO bookHotel(@RequestBody ReservationRequestDTO request) {
        return ReservationService.makeReservation(request);

    }
}