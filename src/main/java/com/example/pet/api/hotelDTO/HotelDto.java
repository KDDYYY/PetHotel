package com.example.pet.api.hotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDto {
    private Long hotelId;
    private String name;
    private String photo;
}
