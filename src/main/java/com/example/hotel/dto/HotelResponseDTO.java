package com.example.hotel.dto;
import lombok.Data;

@Data
public class HotelResponseDTO {
    private Long id;
    private String hotelName;
    private int rating;
    private int weekdayRegular;
    private int weekendRegular;
    private int weekdayReward;
    private int weekendReward;

}
