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
public class PetPhoto {

    @Id
    @GeneratedValue
    @Column(name = "PHOTO_ID")
    private Long id;

    //이미지 url
    @Column(name = "PHOTOPATH")
    private String photo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public PetPhoto(String photo, Pet pet) {
        this.photo = photo;
        this.pet = pet;
    }

}