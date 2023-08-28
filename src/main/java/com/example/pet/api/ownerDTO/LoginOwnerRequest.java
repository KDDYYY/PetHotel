package com.example.pet.api.ownerDTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginOwnerRequest {
    @NotEmpty(message = "이메일을 입력하세요")
    private String email;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String pw;
}
