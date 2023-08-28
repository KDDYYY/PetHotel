package com.example.pet.api.controller;

import com.example.pet.api.ownerDTO.*;
import com.example.pet.domain.Owner;
import com.example.pet.security.JwtTokenProvider;
import com.example.pet.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OwnerAPiController {

    private final OwnerService ownerService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/owners") //회원가입
    public ResponseEntity<CreateOwnerResponse> saveOwner(@RequestBody @Valid CreateOwnerRequest request) { //json 데이터를 member로 변환
        try {
            Owner owner = new Owner(request.getName(), request.getEmail(), request.getPw(), request.getPhoneNum(), request.getIdentity());
            CreateOwnerResponse response = new CreateOwnerResponse();
            //CreateMemberResponse response = new CreateMemberResponse(id);
            Long id = ownerService.join(owner);
            response.setOwnerId(id);
            response.setError("회원가입 성공");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CreateOwnerResponse response = new CreateOwnerResponse();
            response.setOwnerId(null);
            response.setError("회원가입 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //로그인
    @PostMapping("/api/owners/login")
    public ResponseEntity<LoginOwnerResponse> signin(@RequestBody LoginOwnerRequest request) {
        try {
            Owner owner = ownerService.findByEmailLogin(request.getEmail());

            if (!(owner.getPw().equals(request.getPw()))) {
                LoginOwnerResponse loginOwnerResponse = new LoginOwnerResponse(null ,"null", "null", "로그인 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginOwnerResponse);
            }
            Owner findName = ownerService.findOne(owner.getId()); //이름 반환
            LoginOwnerResponse response = new LoginOwnerResponse(owner.getId(), jwtTokenProvider.createToken(owner.getEmail()), findName.getName(), null);
            return ResponseEntity.ok(response);// 로그인 성공시에만 토큰

        } catch (EmptyResultDataAccessException e) {
            LoginOwnerResponse loginOwnerResponse = new LoginOwnerResponse(null, "null", "null", "email 존재하지 않음");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginOwnerResponse);
        }
    }

    //로그아웃
    @PostMapping("/api/owners/logout")
    public ResponseEntity<LogoutOwnerResponse> signout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtTokenProvider.extractToken(authorizationHeader);
        LogoutOwnerResponse logoutOwnerResponse = new LogoutOwnerResponse();

        if (token != null && jwtTokenProvider.validateToken(token)) {
            boolean invalidated = jwtTokenProvider.invalidateToken(token); // 토큰 무효화

            if (invalidated) {
                logoutOwnerResponse.setError("토큰이 성공적으로 무효화되었습니다. 로그아웃 성공");
                return ResponseEntity.ok(logoutOwnerResponse);
            } else {
                logoutOwnerResponse.setError("토큰 무효화 실패");
                return ResponseEntity.badRequest().body(logoutOwnerResponse);
            }
        } else {
            logoutOwnerResponse.setError("유효하지 않은 토큰");
            return ResponseEntity.badRequest().body(logoutOwnerResponse);
        }
    }

    //사업자 조회
    @GetMapping("/api/owners/myinfo")
    public ResponseEntity<OwnerMyInfoResponse> myInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtTokenProvider.extractToken(authorizationHeader);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String ownerEmail = jwtTokenProvider.getUserPk(token);

            Owner owner = ownerService.findByEmailLogin(ownerEmail);

            if (owner != null) {
                OwnerMyInfoResponse myInfoResponse = new OwnerMyInfoResponse(owner.getName(), owner.getEmail(), owner.getPhoneNum(), owner.getStatus());
                return ResponseEntity.ok(myInfoResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //회원 수정(이름만)
    @PutMapping("/api/owners/{id}") //회원 수정
    public UpdateOwnerResponse updateOwner(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateOwnerRequest request) {

        ownerService.update(id, request.getName());
        Owner findOwner = ownerService.findOne(id);
        return new UpdateOwnerResponse(findOwner.getId(), findOwner.getName()); //쿼리를 가져와서 결과 보기 위함
    }

    //모든 회원 조회
    @GetMapping("/api/owners")
    public List<OwnerDto> findOwners() {
        List<Owner> findowners = ownerService.findOwners();
        List<OwnerDto> collect = findowners.stream()
                .map(o -> new OwnerDto(o.getName(), o.getEmail(), o.getStatus())) //Owner entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }
}


