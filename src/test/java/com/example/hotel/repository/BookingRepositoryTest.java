package com.example.hotel.repository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel.entity.Booking;
import com.example.hotel.entity.Hotel;



@SpringBootTest
@Transactional
@ActiveProfiles("test")
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void testCountOverlappingBookings_WithOverlap() {
        
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTotalRooms(5);
        hotel = hotelRepository.save(hotel);

        
        Booking booking = new Booking();
        booking.setHotel(hotel);
        booking.setCheckInDate(LocalDate.of(2025, 12, 1));
        booking.setCheckOutDate(LocalDate.of(2025, 12, 5));
        bookingRepository.save(booking);

        
        LocalDate newCheckIn = LocalDate.of(2025, 12, 4);
        LocalDate newCheckOut = LocalDate.of(2025, 12, 6);
        int overlapCount = bookingRepository.countOverlappingBookings(hotel.getId(), newCheckIn, newCheckOut);

        
        assertThat(overlapCount).isEqualTo(1);
    }

    @Test
    public void testCountOverlappingBookings_NoOverlap() {
        
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTotalRooms(5);
        hotel = hotelRepository.save(hotel);

        Booking booking = new Booking();
        booking.setHotel(hotel);
        booking.setCheckInDate(LocalDate.of(2025, 12, 1));
        booking.setCheckOutDate(LocalDate.of(2025, 12, 5));
        bookingRepository.save(booking);

        
        LocalDate newCheckIn = LocalDate.of(2025, 12, 10);
        LocalDate newCheckOut = LocalDate.of(2025, 12, 12);
        int overlapCount = bookingRepository.countOverlappingBookings(hotel.getId(), newCheckIn, newCheckOut);
        assertThat(overlapCount).isEqualTo(0);
    }}