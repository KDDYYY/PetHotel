package com.example.pet.api.ownerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CreateOwnerRequest {
    @NotEmpty(message = "회원 이름은 필수")
    private String name;

    @NotEmpty(message = "회원 이메일은 필수")
    private String email;

    @NotEmpty(message = "회원 비밀번호는 필수")
    private String pw;

    @NotEmpty(message = "회원 핸드폰 번호는 필수")
    private String phoneNum;

    @NotEmpty(message = "회원 주민등록번호는 필수")
    private String identity;

}
