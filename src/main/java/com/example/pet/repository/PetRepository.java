package com.example.pet.repository;

import com.example.pet.domain.Hotel;
import com.example.pet.domain.Owner;
import com.example.pet.domain.Pet;
import com.example.pet.domain.PetPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PetRepository {
    private final EntityManager em;

    //회원가입
    public void save(Pet pet) {
        em.persist(pet);
    }

    //펫 사진 등록
    public void photoSavePet(PetPhoto petPhoto) {
        em.persist(petPhoto);
    }

    //등록한 펫 목록
    public List<Pet> findList(Long memberId) {
        return em.createQuery("select p from Pet p where p.member.id = :memberId", Pet.class)
                .setParameter("memberId", memberId)
                .getResultList();

    }

    public Pet findOne(Long id) {
        return em.find(Pet.class, id);
    }
}
