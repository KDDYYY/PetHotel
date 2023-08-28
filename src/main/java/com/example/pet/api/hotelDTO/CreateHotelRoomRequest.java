package com.example.pet.api.hotelDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateHotelRoomRequest {
    private String description; //사용 가능 사이즈, 사용 가능 동물 etc..
    private String facility; // 시설
}
