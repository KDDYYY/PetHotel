package com.example.pet.api.hotelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class HotelInfoResponse {
    private String name;
    private String description;
    private String phoneNum;

    private List <String> photos;
}