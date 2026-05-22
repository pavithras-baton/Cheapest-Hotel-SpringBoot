package com.example.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
}
