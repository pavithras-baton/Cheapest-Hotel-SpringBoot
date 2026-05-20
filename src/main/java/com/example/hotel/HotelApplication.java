package com.example.hotel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.hotel.Entity.Hotel;
import com.example.hotel.repository.HotelRepository;

@SpringBootApplication
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}
	@Bean
    public CommandLineRunner loadInitialData(HotelRepository hotelRepository) {
        return args -> {
            
            if (hotelRepository.count() == 0) {
                
                Hotel lakewood = new Hotel();
                lakewood.setName("Lakewood");
                lakewood.setRating(3);
                lakewood.setTotalRooms(3);
                lakewood.setWeekdayRegular(110);
                lakewood.setWeekendRegular(90);
                lakewood.setWeekdayReward(80);
                lakewood.setWeekendReward(80);

                Hotel bridgewood = new Hotel();
                bridgewood.setName("Bridgewood");
                bridgewood.setRating(4);
                bridgewood.setTotalRooms(10);
                bridgewood.setWeekdayRegular(160);
                bridgewood.setWeekendRegular(60);
                bridgewood.setWeekdayReward(110);
                bridgewood.setWeekendReward(50);

                Hotel ridgewood = new Hotel();
                ridgewood.setName("Ridgewood");
                ridgewood.setRating(5);
                ridgewood.setTotalRooms(10);
                ridgewood.setWeekdayRegular(220);
                ridgewood.setWeekendRegular(150);
                ridgewood.setWeekdayReward(100);
                ridgewood.setWeekendReward(40);
                hotelRepository.save(lakewood);
                hotelRepository.save(bridgewood);
                hotelRepository.save(ridgewood);
                
                System.out.println("DATABASE SEEDED WITH 3 HOTELS!");
            }
        };
    }
}
