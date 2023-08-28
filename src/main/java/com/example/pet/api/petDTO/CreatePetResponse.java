package com.example.pet.api.petDTO;


import lombok.Data;

@Data
public class CreatePetResponse {
    private Long petId;
    private String error;

}
