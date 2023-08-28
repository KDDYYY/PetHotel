package com.example.pet.api.memberDTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginMemberRequest {
    @NotEmpty(message = "이메일을 입력하세요")
    private String email;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String pw;
}
