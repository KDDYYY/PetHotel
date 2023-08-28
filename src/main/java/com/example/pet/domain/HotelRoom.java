package com.example.pet.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRoom {

    @Id
    @GeneratedValue
    @Column(name = "HOTEL_ROOM_ID")
    private Long id;

    private String description; //특이사항 (사용 가능 사이즈, 사용 가능 동물 etc.)

    private String facility; // 시설

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public HotelRoom(String description, String facility, Hotel hotel) {
        this.description = description;
        this.facility = facility;
        this.hotel = hotel;
    }
}
