package com.example.pet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel{

    @Id @GeneratedValue
    @Column(name = "HOTEL_ID")
    private Long id;

    //호텔 이름
    @Column(name = "NAME")
    private String name;

    //사업자 번호
    @Column(name = "BSNUM")
    private String bsNum;

    //호텔 설명
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "hotel")
    private List<HotelPhoto> photos = new ArrayList<>();


    @OneToMany(mappedBy = "hotel")
    private List<Reservation> reservations = new ArrayList<>();


    @OneToMany(mappedBy = "hotel")
    private List<HotelRoom> hotelRooms = new ArrayList<>();


    public Hotel(String name, String bsNum, String description) {
        this.name = name;
        this.bsNum = bsNum;
        this.description = description;
    }

    public List<String> getHotelPhotoURLs(){
        List<String> urls = new ArrayList<>();
        for(HotelPhoto hotelPhoto : photos){
                urls.add(hotelPhoto.getPhoto());
        }
        return urls;
    }

    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        for (Reservation reservation : reservations) {
            if (reservation.getStartTime().isBefore(endTime) && reservation.getEndTime().isAfter(startTime)) {
                return false; // 이미 예약되어 있는 경우
            }
        }
        return true; // 예약 가능한 경우
    }




//    public String getMainPhotoPath(){
//        if(photos != null){
//            for(HotelPhoto photo : photos){
//                if(photo.isMainPhoto()){
//                    return photo.getPhotoPath();
//                }
//            }
//        }
//        return null;
//    }

}
