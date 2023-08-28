//package com.example.pet.repository;
//
//import com.example.pet.domain.InvalidToken;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class InvalidTokenRepository {
//    private final EntityManager em;
//
//    //토큰 저장
//    public void save(InvalidToken token) {
//        em.persist(token);
//    }
//
//    public List<InvalidToken> findByToken(String tk) {
//        return em.createQuery("select i from InvalidToken i where i.tk = :tk", InvalidToken.class)
//                .setParameter("tk", tk)
//                .getResultList();
//    }
//
//
//}
