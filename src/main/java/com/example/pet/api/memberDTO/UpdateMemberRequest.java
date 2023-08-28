package com.example.pet.api.memberDTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMemberRequest {

    @NotNull
    private String name;

}
