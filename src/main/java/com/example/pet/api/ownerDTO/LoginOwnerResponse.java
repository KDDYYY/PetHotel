package com.example.pet.api.ownerDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginOwnerResponse {
    private Long ownerId;
    private String token;
    private String name;
    private String error;
}
