package com.example.pet.service;

import com.example.pet.domain.Owner;
import com.example.pet.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository onerRepository;

    private void validateDuplicateOwner(Owner oner) { //중복 이메일 회원 검증
        List<Owner> findOners = onerRepository.findByEmail(oner.getEmail());
        if (!findOners.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public Long join(Owner oner) {
        validateDuplicateOwner(oner); //중복 회원 검증
        onerRepository.save(oner);
        return oner.getId();
    }

    //회원 이름 수정
    @Transactional
    public void update(Long id, String name){
        Owner owner = onerRepository.findOne(id);
        owner.setName(name);
    }


    //회원 전체 조회
    public List<Owner> findOwners() {
        return onerRepository.findAll();
    }

    //회원 하나 조회
    public Owner findOne(Long hotelonerId) {
        return onerRepository.findOne(hotelonerId);
    }



    //로그인 (이메일을 통헤서 비밀번호 찾아옴)
    public Owner findByEmailLogin(String email){
        Owner owner = onerRepository.loginCheck(email);
        return owner;
    }
}
