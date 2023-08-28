package com.example.pet.api.ownerDTO;

import com.example.pet.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerDto {
    private String name;
    private String email;
    private Status status;
}