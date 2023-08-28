package com.example.pet.api.ownerDTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateOwnerRequest {

    @NotNull
    private String name;

}
