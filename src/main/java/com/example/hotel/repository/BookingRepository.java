package com.example.hotel.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.hotel.id = :hotelId AND b.checkInDate < :newCheckOut AND b.checkOutDate > :newCheckIn")
    int countOverlappingBookings(
            @Param("hotelId") Long hotelId, 
            @Param("newCheckIn") LocalDate newCheckIn, 
            @Param("newCheckOut") LocalDate newCheckOut
    );
}
