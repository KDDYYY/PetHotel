package com.example.pet.api.hotelDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HotelRoomInfoResponse {

    private String description; //특이사항 (사용 가능 사이즈, 사용 가능 동물 etc.)
    private String facility; // 시설
}
