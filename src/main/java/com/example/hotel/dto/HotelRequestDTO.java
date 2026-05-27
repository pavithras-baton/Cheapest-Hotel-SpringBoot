package com.example.hotel.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelRequestDTO {
    private String name;
    private int rating;
    private double regularWeekdayPrice;
    private double regularWeekendPrice;
    private double rewardWeekdayPrice;
    private double rewardWeekendPrice;
}
