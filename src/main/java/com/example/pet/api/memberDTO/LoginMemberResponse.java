package com.example.pet.api.memberDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginMemberResponse {
    private Long id; //자신이 등록한게 뭐인지 리스트를 뽑기 위해 pk값 알 필요가 있음
    private String token;
    private String name;
    private String error;
}
