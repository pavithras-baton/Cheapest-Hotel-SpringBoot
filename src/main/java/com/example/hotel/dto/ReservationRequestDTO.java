package com.example.hotel.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationRequestDTO {
    private String customerType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
