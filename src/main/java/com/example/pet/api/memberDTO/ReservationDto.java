package com.example.pet.api.memberDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationDto {

    private String hotelName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long hotelId;

}
