package com.example.hotel.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter
@Setter
public class Hotel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private int rating;
    
    @Column(name="total_rooms") 
    private int totalRooms = 2;

    @Column(name = "regular_weekday_price")
    private double regularWeekdayPrice;

    @Column(name = "regular_weekend_price")
    private double regularWeekendPrice;

    @Column(name = "reward_weekday_price")
    private double rewardWeekdayPrice;

    @Column(name = "reward_weekend_price")
    private double rewardWeekendPrice;
}
