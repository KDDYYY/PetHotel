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
public class HotelPhoto {

    @Id @GeneratedValue
    @Column(name = "PHOTO_ID")
    private Long id;

    //이미지 url
    @Column(name = "PHOTOPATH")
    private String photo;


//    //대표 이미지 분별
//    @Column(name = "IS_MAIN_PHOTO")
//    private boolean mainPhoto;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public HotelPhoto(String photo, Hotel hotel) {
        this.photo = photo;
        this.hotel = hotel;
    }

}
