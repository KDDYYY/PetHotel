package com.example.pet.api.ownerDTO;

import com.example.pet.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OwnerMyInfoResponse {
    private String name;
    private String email;
    private String phoneNum;
    private Status status;

}
