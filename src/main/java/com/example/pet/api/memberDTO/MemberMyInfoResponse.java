package com.example.pet.api.memberDTO;

import com.example.pet.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberMyInfoResponse {
    private String name;
    private String email;
    private String phonenum;
    private Status status;

}
