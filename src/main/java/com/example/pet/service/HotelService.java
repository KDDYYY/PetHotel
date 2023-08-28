package com.example.pet.service;

import com.example.pet.domain.*;
import com.example.pet.repository.HotelRepository;
import com.example.pet.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HotelService{

    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;

    //중복 사업자 번호 방지
    private void validateDuplicateBsNUm(Hotel hotel) {
        List<Hotel> findbsNum = hotelRepository.findByBsNum(hotel.getBsNum());
        if (!findbsNum.isEmpty()) {
            throw new IllegalStateException("중복되는 사업자 번호입니다");
        }
    }

    //호텔 등록
    @Transactional
    public Long register(Hotel hotel, Owner owner, List<String> photos){
        validateDuplicateBsNUm(hotel);

        hotel.setOwner(owner);
        hotelRepository.save(hotel);

        owner.getHotel().add(hotel);

        for (String photoUrl : photos) {
            HotelPhoto photo = new HotelPhoto(photoUrl, hotel);
            registerPhoto(hotel, photo);
        }

        return hotel.getId();
    }

    //호텔 이미지 등록
    @Transactional
    public void registerPhoto(Hotel hotel, HotelPhoto hotelPhoto){
        hotelPhoto.setHotel(hotel);
        hotelRepository.photoSaveHotel(hotelPhoto);

        hotel.getPhotos().add(hotelPhoto); //List에 등록
    }

    //호텔 수정을 위한 호텔등록 다시하기
    @Transactional
    public void modify(Long id){
        //Hotel hotel = hotelRepository.findOne(id);
        hotelRepository.modifyHotel(id);
    }

    //호텔 객실 등록
    @Transactional
    public Long registerRoom(Hotel hotel, HotelRoom hotelRoom){
        hotelRoom.setHotel(hotel);
        hotelRepository.saveHotelRoom(hotelRoom);

        hotel.getHotelRooms().add(hotelRoom);

        return hotelRoom.getId();
    }


    //등록한 호텔 조회
    public List<Hotel> findHotelList(Long ownerId) {
        return hotelRepository.findHotelList(ownerId);
    }

    public List<HotelRoom> findHotelRoomList(Long hotelId){
        return hotelRepository.findHotelRoomList(hotelId);
    }


    //호텔 전체 조회
    public List<Hotel> findHotels() {
        return hotelRepository.findAll();
    }

    //호텔 id로 조회
    public Hotel findOne(Long hotelId) {
        return hotelRepository.findOne(hotelId);
    }

    //호텔 객실 id로 조회
    public HotelRoom findRoom(Long hotelRoomId) {return hotelRepository.findRoom(hotelRoomId);}


    //에약 기능
    @Transactional
    public Long createReservation(Hotel hotel, Member member, LocalDateTime startTime, LocalDateTime endTime){

        Reservation reservation = Reservation.builder()
                .startTime(startTime)
                .endTime(endTime)
                .hotel(hotel)
                .member(member)
                .build();

        hotelRepository.saveReservation(reservation);

        hotel.getReservations().add(reservation);
        member.getReservations().add(reservation);

        return reservation.getId();
    }

    public Reservation findResveration(Long reservaionId){return  hotelRepository.findReservation(reservaionId);}

}
