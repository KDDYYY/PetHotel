package com.example.pet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id @GeneratedValue
    @Column(name = "PET_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SIGNIFICANT")
    private String significant; //특이사항

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "pet")
    private List<PetPhoto> photos = new ArrayList<>();


    public Pet(String name, String significant) {
        this.name = name;
        this.significant = significant;
    }

    public List<String> getPetPhotoURLs(){
        List<String> urls = new ArrayList<>();
        for(PetPhoto petPhoto : photos){
            urls.add(petPhoto.getPhoto());
        }
        return urls;
    }

}
