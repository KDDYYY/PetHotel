package com.example.pet.api.controller;

import com.example.pet.api.memberDTO.*;
import com.example.pet.domain.Member;
import com.example.pet.domain.Reservation;
import com.example.pet.security.JwtTokenProvider;
import com.example.pet.service.MemberService;
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
public class MemberApiController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    //private final InvalidTokenService invalidTokenService;

    //회원가입
    @PostMapping("/api/members") //회원가입
    public ResponseEntity<CreateMemberResponse> saveMember(@RequestBody @Valid CreateMemberRequest request){ //json 데이터를 member로 변환
        try {
            Member member = new Member(request.getName(),request.getEmail(), request.getPw(), request.getPhonenum(), request.getIdentity());
            CreateMemberResponse response = new CreateMemberResponse();
            Long id = memberService.join(member);
            response.setId(id);
            response.setError("회원가입 성공");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CreateMemberResponse response = new CreateMemberResponse();
            response.setId(null);
            response.setError("회원가입 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //로그인
    @PostMapping("/api/members/login")
    public ResponseEntity<LoginMemberResponse> signin(@RequestBody LoginMemberRequest request){
        try {
            Member member = memberService.findByEmailLogin(request.getEmail());

            if(!(member.getPw().equals(request.getPw()))){
                LoginMemberResponse loginMemberResponse = new LoginMemberResponse(null,"null","null", "로그인 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginMemberResponse);
            }
            Member findName = memberService.findOne(member.getId()); //이름 반환

            String tk = jwtTokenProvider.createToken(member.getEmail()); //토큰 생성

//            InvalidToken invalidToken = new InvalidToken(tk); //DB에 토큰 저장
//            invalidTokenService.register(invalidToken);

            LoginMemberResponse response = new LoginMemberResponse(member.getId(), tk, findName.getName(), null);

            return ResponseEntity.ok(response);// 로그인 성공시에만 토큰

        }catch (EmptyResultDataAccessException e){
            LoginMemberResponse loginMemberResponse = new LoginMemberResponse(null ,"null","null", "email 존재하지 않음");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginMemberResponse);
        }
    }

    //로그아웃
    @PostMapping("/api/members/logout")
    public ResponseEntity<LogoutMemberResponse> signout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtTokenProvider.extractToken(authorizationHeader);
        LogoutMemberResponse logoutMemberResponse = new LogoutMemberResponse();

        if (token != null && jwtTokenProvider.validateToken(token)) {
            boolean invalidated = jwtTokenProvider.invalidateToken(token); // 토큰 무효화

            if (invalidated) {
                logoutMemberResponse.setError("토큰이 성공적으로 무효화되었습니다. 로그아웃 성공");
                return ResponseEntity.ok(logoutMemberResponse);
            } else {
                logoutMemberResponse.setError("토큰 무효화 실패");
                return ResponseEntity.badRequest().body(logoutMemberResponse);
            }
        } else {
            logoutMemberResponse.setError("유효하지 않은 토큰");
            return ResponseEntity.badRequest().body(logoutMemberResponse);
        }
    }

    //회원 조회
    @GetMapping("/api/members/myinfo")
    public ResponseEntity<MemberMyInfoResponse> myinfo(@RequestHeader("Authorization") String authorizationHeader){
        String token = jwtTokenProvider.extractToken(authorizationHeader);

        if(token != null && jwtTokenProvider.validateToken(token)) {
            String memberEmail = jwtTokenProvider.getUserPk(token);

            Member member = memberService.findByEmailLogin(memberEmail);

            if (member != null) {
                MemberMyInfoResponse myInfoResponse = new MemberMyInfoResponse(member.getName(), member.getEmail(), member.getPhonenum(), member.getStatus());
                return ResponseEntity.ok(myInfoResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //회원 수정(이름만)
    @PutMapping("/api/members/{id}") //회원 수정
    public UpdateMemberResponse updateMember(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName()); //쿼리를 가져와서 결과 보기 위함
    }

    //모든 회원 조회
    @GetMapping("/api/members")
    public List<MemberDto> findmembers(){
        List<Member> findmembers = memberService.findMembers();
        List<MemberDto> collect = findmembers.stream()
                .map(m -> new MemberDto(m.getName(), m.getEmail() , m.getStatus())) //Member entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }


    //예약한 목록
    @GetMapping("/api/members/res")
    public List<ReservationDto> reservationList(@RequestHeader("Authorization") String authorizationHeader){

        String token = jwtTokenProvider.extractToken(authorizationHeader);
        String memberEmail = jwtTokenProvider.getUserPk(token);

        Member member = memberService.findByEmailLogin(memberEmail);
        List<Reservation> res = member.getReservations();

        List<ReservationDto> collect = res.stream()
                .map(m -> new ReservationDto(m.getHotel().getName(), m.getStartTime() , m.getEndTime(), m.getHotel().getId())) //Member entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }




//    @Data
//    @AllArgsConstructor
//    static class Result<T>{ //json 배열 타입으로 나가지 않게 하기 위해
//        private int count;
//        private T data;
//    }

    //로그인
//    @PostMapping("/api/members/login")
//    public LoginMemberResponse signin(@RequestBody LoginMemberRequest request){
//            Member member = memberService.findByEmailLogin(request.getEmail());
//            if(member != null && member.getPw().equals(request.getPw())){
//                return new LoginMemberResponse(member.getId());
//            }else{
//                return new LoginMemberResponse(0L); //로그인 실패
//            }
//        }

//    @PostMapping("/api/members/login")
//    public ResponseEntity<Map<String,String>> signin(@RequestBody LoginMemberRequest request){
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            Member member = memberService.findByEmailLogin(request.getEmail());
//
//            if(!(member.getPw().equals(request.getPw()))){
//                response.put("error", "비밀번호 틀림");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//            Member findName = memberService.findOne(member.getId()); //이름 반환
//
//            response.put("token",jwtTokenProvider.createToken(member.getEmail()));
//            response.put("name", findName.getName());
//
//            return ResponseEntity.ok(response); // 로그인 성공시에만 토큰
//        }catch (EmptyResultDataAccessException e){
//            response.put("error", "email 존재하지 않음");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }

}
