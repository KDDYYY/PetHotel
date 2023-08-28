package com.example.pet.api.ownerDTO;

import lombok.Data;

@Data
public class CreateOwnerResponse {
    private Long ownerId;
    private String error;
}