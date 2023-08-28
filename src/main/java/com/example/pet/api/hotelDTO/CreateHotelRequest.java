package com.example.pet.api.hotelDTO;

import com.example.pet.domain.HotelPhoto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateHotelRequest {

    @NotEmpty(message = "호텔 이름은 필수")
    private String name;

    @NotEmpty(message = "사업자 번호는 필수")
    private String bsNum;

    //호텔 설명
    private String description;

    private List<MultipartFile> imageFiles;

}
