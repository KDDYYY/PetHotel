package com.example.pet.service;


import com.example.pet.domain.*;
import com.example.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    //펫 등록
    @Transactional
    public Long register(Member member, Pet pet, List<String> photos){
        //validateDuplicateBsNUm(hotel);

        pet.setMember(member);

        petRepository.save(pet);

        member.getPets().add(pet);

        for (String photoUrl : photos) {
            PetPhoto photo = new PetPhoto(photoUrl, pet);
            registerPhoto(pet, photo);
        }

        return pet.getId();
    }

    //펫 이미지 등록
    @Transactional
    public void registerPhoto(Pet pet, PetPhoto petPhoto){
        petPhoto.setPet(pet);
        petRepository.photoSavePet(petPhoto);

        pet.getPhotos().add(petPhoto);
    }


    //등록한 펫 조회
    public List<Pet> findPetList(Long memberId) {
        return petRepository.findList(memberId);
    }

    //호텔 id로 조회
    public Pet findOne(Long petId) {
        return petRepository.findOne(petId);
    }

}
