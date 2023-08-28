package com.example.pet.repository;


import com.example.pet.domain.Hotel;
import com.example.pet.domain.HotelPhoto;
import com.example.pet.domain.HotelRoom;
import com.example.pet.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HotelRepository {

    private final EntityManager em;

    public void save(Hotel hotel) {
        em.persist(hotel);
    }

    public void photoSaveHotel(HotelPhoto hotelPhoto){
        em.persist(hotelPhoto);
    }

    public void saveHotelRoom(HotelRoom hotelRoom){
        em.persist(hotelRoom);
    }

    //수정을 위한 호텔 삭제
    public void modifyHotel(Long id){
//        em.createQuery("delete from HotelPhoto h where h.hotel.id =: id", HotelPhoto.class)
//                        .setParameter("id", id)
//                        .executeUpdate();
//
//        em.createQuery("delete from Hotel h where h.id = : id", Hotel.class)
//                .setParameter("id", id)
//                .executeUpdate();

        em.createQuery("DELETE FROM HotelPhoto h WHERE h.hotel.id IN (SELECT h.id FROM Hotel h WHERE h.id = :id)")
                .setParameter("id", id)
                .executeUpdate();

        em.createQuery("delete from Hotel h where h.id = : id")
                .setParameter("id", id)
                .executeUpdate();
    }


    public List<Hotel> findHotelList(Long ownerId) {
        return em.createQuery("select h from Hotel h where h.owner.id = :ownerId", Hotel.class)
                .setParameter("ownerId", ownerId)
                .getResultList();

    }

    public List<HotelRoom> findHotelRoomList(Long hotelId) {
        return em.createQuery("select h from HotelRoom h where h.hotel.id = :hotelId", HotelRoom.class)
                .setParameter("hotelId", hotelId)
                .getResultList();

    }




    //호텔 사업자 번호 중복 여부
    public List<Hotel> findByBsNum(String bsNum) {
        return em.createQuery("select h from Hotel h where h.bsNum = :bsNum", Hotel.class)
                .setParameter("bsNum", bsNum)
                .getResultList();
    }


    //호텔 목록
    //회원 이메일 중복 체크
    //회원 목록
    public List<Hotel> findAll() {
        return em.createQuery("select h from Hotel h", Hotel.class)
                .getResultList();
    }

//    public List<Hotel> findHotelsByPage(int pageNumber, int pageSize) {
//        return em.createQuery("select h from Hotel h", Hotel.class)
//                .setFirstResult((pageNumber - 1) * pageSize)
//                .setMaxResults(pageSize)
//                .getResultList();
//    }

    //List<Hotel> hotels = findHotelsByPage(1, 10);

    public Hotel findOne(Long hotelId) {
        return em.find(Hotel.class, hotelId);
    }

    public HotelRoom findRoom(Long hotelRoomId) {
        return em.find(HotelRoom.class, hotelRoomId);
    }


    //예약 기능
    public void saveReservation(Reservation reservation){
        em.persist(reservation);
    }


    //예약 찾기
    public Reservation findReservation(Long reservationId){
        return em.find(Reservation.class, reservationId);
    }

    //호텔 검색 기능



}
