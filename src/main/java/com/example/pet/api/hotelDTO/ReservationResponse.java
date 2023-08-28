package com.example.pet.api.hotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationID;
    private String error;
}
