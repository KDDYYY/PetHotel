package com.example.pet.api.memberDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMemberResponse{
    private Long id;
    private String name;
}