package com.example.hotel.service;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.hotel.dto.HotelResponseDTO;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;
import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.HotelRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ReservationService reservationService;
    @Test
    public void makeReservation_regularcustomer() {
        Hotel expensiveHotel = new Hotel();
        expensiveHotel.setName("Expensive");
        expensiveHotel.setTotalRooms(5);
        expensiveHotel.setWeekdayRegular(500.0);

        Hotel cheapHotel = new Hotel();
        cheapHotel.setName("Cheap");
        cheapHotel.setTotalRooms(5);
        cheapHotel.setWeekdayRegular(100.0);
        
        when(hotelRepository.findAll()).thenReturn(List.of(expensiveHotel, cheapHotel));
        when(bookingRepository.countOverlappingBookings(any(), any(), any())).thenReturn(0);

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerType("Regular");
        request.setCheckInDate(LocalDate.of(2025, 12, 1));
        request.setCheckOutDate(LocalDate.of(2025, 12, 2)); 
        ReservationResponseDTO response = reservationService.makeReservation(request);
        assertThat(response.getHotelName()).isEqualTo("Cheap");
        assertThat(response.getTotalCost()).isEqualTo(100);
        assertThat(response.getMessage()).isEqualTo("Reservation successful");
    }

    @Test
    public void makeReservation_rewardscustomer() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Reward Hotel");
        hotel.setTotalRooms(5);
        hotel.setWeekdayRegular(500.0); // Very expensive for regular
        hotel.setWeekdayReward(50.0);   // Very cheap for rewards
        
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(bookingRepository.countOverlappingBookings(any(), any(), any())).thenReturn(0);

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerType("Rewards"); // Triggering the Rewards pricing
        request.setCheckInDate(LocalDate.of(2025, 12, 1));
        request.setCheckOutDate(LocalDate.of(2025, 12, 2));

        
        ReservationResponseDTO response = reservationService.makeReservation(request);

        
        assertThat(response.getTotalCost()).isEqualTo(50);
    }
    @Test
    public void makeReservation_WhenCostsAreEqual() {
        Hotel lowRated = new Hotel();
        lowRated.setName("Low Rated Hotel");
        lowRated.setRating(2);
        lowRated.setTotalRooms(5);
        lowRated.setWeekdayRegular(100.0);

        Hotel highRated = new Hotel();
        highRated.setName("High Rated Hotel");
        highRated.setRating(5);
        highRated.setTotalRooms(5);
        highRated.setWeekdayRegular(100.0); 
        
        when(hotelRepository.findAll()).thenReturn(List.of(lowRated, highRated));
        when(bookingRepository.countOverlappingBookings(any(), any(), any())).thenReturn(0);

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerType("Regular");
        request.setCheckInDate(LocalDate.of(2025, 12, 1));
        request.setCheckOutDate(LocalDate.of(2025, 12, 2));
        ReservationResponseDTO response = reservationService.makeReservation(request);

        assertThat(response.getHotelName()).isEqualTo("High Rated Hotel");
    }
    @Test
    public void makeReservation_invalidCustomerType() {
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerType("Premium"); 
        request.setCheckInDate(LocalDate.of(2025, 12, 1));
        request.setCheckOutDate(LocalDate.of(2025, 12, 2));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(request);
        });
        assertThat(exception.getMessage()).contains("Invalid customer type. Expected 'Regular' or 'Rewards'.");
    }
    @Test
    public void makeReservation_invalidDates() {
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setCustomerType("Regular");
        request.setCheckInDate(LocalDate.of(2025, 12, 5));
        request.setCheckOutDate(LocalDate.of(2025, 12, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(request);
        });
        assertThat(exception.getMessage()).contains("Check-in date must be before check-out date.");
    }
    @Test
    public void getAllHotels_DTOs() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Secure Hotel");
        hotel.setRating(4);
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        List<HotelResponseDTO> result = reservationService.getAllHotels();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHotelName()).isEqualTo("Secure Hotel");
        assertThat(result.get(0).getRating()).isEqualTo(4);
    }
}