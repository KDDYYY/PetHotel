package com.example.pet.repository;

import com.example.pet.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerRepository {

    private final EntityManager em;

    //회원가입
    public void save(Owner hotelOwner) {
        em.persist(hotelOwner);
    }

    //회원 id로 조회 조회
    public Owner findOne(Long id) {
        return em.find(Owner.class, id);
    }


    //회원 목록
    public List<Owner> findAll() {
        return em.createQuery("select o from Owner o", Owner.class)
                .getResultList();
    }

    //회원 이메일 중복 체크
    public List<Owner> findByEmail(String email) {
        return em.createQuery("select o from Owner o where o.email = :email", Owner.class)
                .setParameter("email", email)
                .getResultList();
    }

    //로그인을 위해 이메일 불러오기
    public Owner loginCheck(String email) {
        return em.createQuery("select o from Owner o where o.email = :email", Owner.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
