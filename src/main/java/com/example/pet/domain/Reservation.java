package com.example.pet.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "RS_ID")
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //private LocalDateTime orderDate; // 예약 했을 때 시간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
