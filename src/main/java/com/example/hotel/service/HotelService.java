package com.example.hotel.service;
import org.springframework.stereotype.Service;

import com.example.hotel.dto.HotelRequestDTO;
import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.HotelRepository;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public String addHotel(HotelRequestDTO request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setRating(request.getRating());
        hotel.setRegularWeekdayPrice(request.getRegularWeekdayPrice());
        hotel.setRegularWeekendPrice(request.getRegularWeekendPrice());
        hotel.setRewardWeekdayPrice(request.getRewardWeekdayPrice());
        hotel.setRewardWeekendPrice(request.getRewardWeekendPrice());

        hotelRepository.save(hotel);
        return "Hotel '" + hotel.getName() + "' successfully added to the database!";
    }
}
