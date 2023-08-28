package com.example.pet.api.hotelDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
