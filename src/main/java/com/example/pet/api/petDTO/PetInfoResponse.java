package com.example.pet.api.petDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetInfoResponse {
    private String name;
    private String significant; //특이사항
    private String animalType;

    private List<String> photos;
}
