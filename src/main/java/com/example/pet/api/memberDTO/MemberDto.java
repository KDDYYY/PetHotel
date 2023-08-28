package com.example.pet.api.memberDTO;

import com.example.pet.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto{
    private String name;
    private String email;
    private Status status;
}