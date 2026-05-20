package com.example.hotel.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.hotel.Entity.Booking;
import com.example.hotel.Entity.Hotel;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;
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
        String input = request.getRawInput();

        if (input == null || input.trim().isEmpty()) {
        throw new IllegalArgumentException("Input cannot be empty. Expected format: 'CustomerType: 1Dec2025, 2Dec2025'");
        }
        if (!input.contains(":")) {
        throw new IllegalArgumentException("Missing colon separator. Expected format: 'CustomerType: 1Dec2025, 2Dec2025'");
        }

        String[] parts = input.split(":");
        String customerType = parts[0].trim();
    
        if (!customerType.equalsIgnoreCase("Regular") && 
        !customerType.equalsIgnoreCase("Reward") && 
        !customerType.equalsIgnoreCase("Rewards")) {
        throw new IllegalArgumentException("Invalid customer type. Must be 'Regular' or 'Reward'.");
        }

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
        throw new IllegalArgumentException("No dates provided after the colon.");
        }

        String[] dateParts = parts[1].split(",");
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dMMMyyyy", Locale.ENGLISH);
        for (String datePart : dateParts) {
            LocalDate date = LocalDate.parse(datePart.trim(), formatter);
            dates.add(date);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendCount++;
            } 
            else {
                weekdayCount++;
            }
        }
        LocalDate checkIn = dates.get(0);
        LocalDate checkOut = dates.get(dates.size() - 1).plusDays(1);
        List<Hotel> hotels = hotelRepository.findAll();
        Hotel cheapestHotel = null;
        int minCost = Integer.MAX_VALUE;
        for (Hotel hotel : hotels) {
            int overlapCount = bookingRepository.countOverlappingBookings(hotel.getId(), checkIn, checkOut);
            if (overlapCount < hotel.getTotalRooms()) {
                int cost = 0;
                if (customerType.equalsIgnoreCase("Regular")) {
                    cost = weekdayCount * hotel.getWeekdayRegular() + weekendCount * hotel.getWeekendRegular();
                } else if (customerType.equalsIgnoreCase("Reward")) {
                    cost = weekdayCount * hotel.getWeekdayReward() + weekendCount * hotel.getWeekendReward();
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
}