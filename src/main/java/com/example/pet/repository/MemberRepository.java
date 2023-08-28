package com.example.pet.repository;

import com.example.pet.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //회원가입
    public void save(Member member) {
        em.persist(member);
    }

    //회원 id로 조회 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }


    //회원 목록
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //회원 이메일 중복 체크
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    //로그인을 위해 이메일 불러오기
    public Member loginCheck(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }


}
