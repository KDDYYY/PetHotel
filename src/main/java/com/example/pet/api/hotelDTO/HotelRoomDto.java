package com.example.pet.api.hotelDTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelRoomDto {
    private Long hotelRoomId;

    private String description; //특이사항 (사용 가능 사이즈, 사용 가능 동물 etc.)

    private String facility; // 시설
}
