package com.example.hotel.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hotel.dto.HotelResponseDTO;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;
import com.example.hotel.entity.Booking;
import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.HotelRepository;

@Service

public class ReservationService {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    public ReservationService(HotelRepository hotelRepository, BookingRepository bookingRepository) {
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
        }
    public ReservationResponseDTO makeReservation(ReservationRequestDTO request) {
        int weekdayCount = 0;
        int weekendCount = 0;
        String customerType = request.getCustomerType();
        LocalDate checkIn = request.getCheckInDate();
        LocalDate checkOut = request.getCheckOutDate();

        if (customerType == null || 
           (!customerType.equalsIgnoreCase("Regular") && 
            !customerType.equalsIgnoreCase("Rewards"))) {
            throw new IllegalArgumentException("Invalid customer type. Expected 'Regular' or 'Rewards'.");
        }

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendCount++;
            } 
            else {
                weekdayCount++;
            }
        }

        List<Hotel> hotels = hotelRepository.findAll();
        Hotel cheapestHotel = null;
        double minCost = Double.MAX_VALUE;
        for (Hotel hotel : hotels) {
            int overlapCount = bookingRepository.countOverlappingBookings(hotel.getId(), checkIn, checkOut);
            if (overlapCount < hotel.getTotalRooms()) {
                double cost = 0;
                if (customerType.equalsIgnoreCase("Regular")) {
                    cost = (double) weekdayCount * hotel.getWeekdayRegular() + (double) weekendCount * hotel.getWeekendRegular();
                } else if (customerType.equalsIgnoreCase("Rewards")) {
                    cost = (double) weekdayCount * hotel.getWeekdayReward() + (double) weekendCount * hotel.getWeekendReward();
                }
                if (cost < minCost) {
                    minCost = cost;
                    cheapestHotel = hotel;
                }
                else if (cost == minCost && hotel.getRating() > cheapestHotel.getRating()) {
                    cheapestHotel = hotel;

            }
        }
        }
        ReservationResponseDTO response = new ReservationResponseDTO();
        if (cheapestHotel != null) {
            Booking newBooking = new Booking();
            newBooking.setHotel(cheapestHotel);
            newBooking.setCheckInDate(checkIn);
            newBooking.setCheckOutDate(checkOut);
            bookingRepository.save(newBooking);

            response.setHotelName(cheapestHotel.getName());
            response.setTotalCost(minCost);
            response.setRating(cheapestHotel.getRating());
            response.setMessage("Reservation successful");
        } 
        else {
            response.setMessage("No available hotels for the given dates");
        }
        return response;
    }
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> rawHotels = hotelRepository.findAll();
        
    
        List<HotelResponseDTO> safeHotels = new ArrayList<>();
        for (Hotel hotel : rawHotels) {
            HotelResponseDTO dto = new HotelResponseDTO();
            dto.setId(hotel.getId());
            dto.setHotelName(hotel.getName());
            dto.setRating(hotel.getRating());
            dto.setWeekdayRegular(hotel.getWeekdayRegular());
            dto.setWeekendRegular(hotel.getWeekendRegular());
            dto.setWeekdayReward(hotel.getWeekdayReward());
            dto.setWeekendReward(hotel.getWeekendReward());
            
            safeHotels.add(dto);
        }
        return safeHotels;
    }
}