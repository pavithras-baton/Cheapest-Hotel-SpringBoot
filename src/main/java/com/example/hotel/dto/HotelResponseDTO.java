package com.example.hotel.dto;
import lombok.Data;

@Data
public class HotelResponseDTO {
    private Long id;
    private String hotelName;
    private int rating;
    private double weekdayRegular;
    private double weekendRegular;
    private double weekdayReward;
    private double weekendReward;

}
