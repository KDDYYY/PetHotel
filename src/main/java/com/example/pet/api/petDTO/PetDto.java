package com.example.pet.api.petDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetDto {
    private Long petId;
    private String name;
    private String photo;
}
