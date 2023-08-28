package com.example.pet.api.ownerDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOwnerResponse {
    private Long id;
    private String name;
}