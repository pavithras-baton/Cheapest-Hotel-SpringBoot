package com.example.hotel.controller;


import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hotel.dto.HotelResponseDTO;
import com.example.hotel.dto.ReservationRequestDTO;
import com.example.hotel.dto.ReservationResponseDTO;
import com.example.hotel.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

private ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    @MockitoBean
    private ReservationService reservationService;
    @Test
    public void testBookHotel() throws Exception {
        ReservationRequestDTO reservationrequest = new ReservationRequestDTO();
        reservationrequest.setCustomerType("Regular");
        reservationrequest.setCheckInDate(LocalDate.of(2025, 12, 1));
        reservationrequest.setCheckOutDate(LocalDate.of(2025, 12, 2));

        ReservationResponseDTO reservationresponse = new ReservationResponseDTO();
        reservationresponse.setHotelName("Lakewood");
        reservationresponse.setTotalCost(225);
        reservationresponse.setRating(3);
        reservationresponse.setMessage("Reservation successful");

        when(reservationService.makeReservation(any(ReservationRequestDTO.class))).thenReturn(reservationresponse);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationrequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelName").value("Lakewood"))
                .andExpect(jsonPath("$.totalCost").value(225))
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.message").value("Reservation successful"));

    }
    @Test
    public void testBadRequest() throws Exception {


        when(reservationService.makeReservation(any(ReservationRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid customer type. Expected 'Regular' or 'Rewards'."));

        ReservationRequestDTO reservationrequest = new ReservationRequestDTO();
        reservationrequest.setCustomerType("Happy");
        reservationrequest.setCheckInDate(LocalDate.of(2025, 12, 1));
        reservationrequest.setCheckOutDate(LocalDate.of(2025, 12, 2));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationrequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400 BAD REQUEST"))                
                .andExpect(jsonPath("$.message").value("Invalid customer type. Expected 'Regular' or 'Rewards'."));    }

    @Test
    public void testGetAvailableHotels() throws Exception {
        HotelResponseDTO hotel1 = new HotelResponseDTO();
        hotel1.setId(1L);
        hotel1.setHotelName("Lakewood");
        hotel1.setRating(3);

        HotelResponseDTO hotel2 = new HotelResponseDTO();
        hotel2.setId(2L);
        hotel2.setHotelName("Bridgewood");
        hotel2.setRating(4);

        when(reservationService.getAllHotels()).thenReturn(List.of(hotel1, hotel2));

        mockMvc.perform(get("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hotelName").value("Lakewood"))
                .andExpect(jsonPath("$[0].rating").value(3))
                .andExpect(jsonPath("$[1].hotelName").value("Bridgewood"))
                .andExpect(jsonPath("$[1].rating").value(4));
    }
}
