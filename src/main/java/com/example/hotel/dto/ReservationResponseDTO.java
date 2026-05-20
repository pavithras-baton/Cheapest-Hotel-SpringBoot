package com.example.hotel.dto;
import lombok.Data;
@Data
public class ReservationResponseDTO {
    private String hotelName;
    private int totalCost;
    private int rating;
    private String message;
    
}
