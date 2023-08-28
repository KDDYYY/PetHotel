//package com.example.pet.service;
//
//import com.example.pet.domain.InvalidToken;
//import com.example.pet.repository.InvalidTokenRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class InvalidTokenService {
//
//    private final InvalidTokenRepository invalidTokenTokenRepository;
//
//    @Transactional
//    public void register(InvalidToken invalidToken) {
//        validateDuplicateToken(invalidToken); //중복 회원 검증
//        invalidTokenTokenRepository.save(invalidToken);
//    }
//
//    //DB에 해당 토큰 있는지
//    private void validateDuplicateToken(InvalidToken invalidToken) { //중복 이메일 회원 검증
//        List<InvalidToken> findTokens = invalidTokenTokenRepository.findByToken(invalidToken.getTk());
//        if (!findTokens.isEmpty()) {
//            throw new IllegalStateException("만료된 토큰");
//        }
//    }
//}
