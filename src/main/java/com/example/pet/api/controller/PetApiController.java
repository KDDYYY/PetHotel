package com.example.pet.api.controller;


import com.example.pet.api.petDTO.CreatePetResponse;
import com.example.pet.api.petDTO.PetDto;
import com.example.pet.api.petDTO.PetInfoResponse;
import com.example.pet.domain.*;
import com.example.pet.s3.S3Service;
import com.example.pet.security.JwtTokenProvider;
import com.example.pet.service.MemberService;
import com.example.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PetApiController {

    private final PetService petService;

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    private final S3Service s3Service;

    private static final Logger logger = LoggerFactory.getLogger(PetApiController.class);

    //펫 등록
    @PostMapping("/api/pet")
    public ResponseEntity<CreatePetResponse> saveHotel(@RequestParam("name") String name, @RequestParam("significant") String significant, @RequestParam("animalType") String animalType,
                                                       @RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestPart("imageFiles") List<MultipartFile> imageFiles) {
        try {
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            CreatePetResponse createPetResponse = new CreatePetResponse();
            String memberEmail = jwtTokenProvider.getUserPk(token);

            Member member = memberService.findByEmailLogin(memberEmail);

            if(member.getStatus() == Status.USER){
                Pet pet = new Pet(name, significant);

                if(animalType.equals("CAT")){
                    pet.setAnimalType(AnimalType.CAT);
                }else if(animalType.equals("DOG")){
                    pet.setAnimalType(AnimalType.DOG);
                }else{
                    pet.setAnimalType(AnimalType.ETC);
                }

                ///이미지 등록부분
                List<String> imageUrls = new ArrayList<>(); //이미지 URL 값
                for(MultipartFile imageFile : imageFiles){
                    String imageUrl = s3Service.saveFile(imageFile);
                    imageUrls.add(imageUrl);
                }
                //-------

                Long id = petService.register(member, pet, imageUrls);

                createPetResponse.setPetId(id);
                createPetResponse.setError("펫 등록 성공");

                return ResponseEntity.ok(createPetResponse);
            } else {
                //잘못된 접근 (사용자 아닐 경우)
                createPetResponse.setPetId(null);
                createPetResponse.setError("잘못된 접근");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createPetResponse);
            }
        } catch (Exception e) {
            logger.error("펫 등록 실패 - 요청 정보: {}", e); // 예외 객체 e와 요청 정보를 로그에 출력
            CreatePetResponse createPetResponse = new CreatePetResponse();

            createPetResponse.setPetId(null);
            createPetResponse.setError("펫 등록 실패");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createPetResponse);
        }
    }


    //등록한 펫 목록
    @GetMapping("/api/pets/{memberId}")
    public List<PetDto> findHotels(@RequestHeader("Authorization") String authorizationHeader,
                                   @PathVariable("memberId")Long memberId) {

        try {
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            String memberEmail = jwtTokenProvider.getUserPk(token);

            Member member = memberService.findByEmailLogin(memberEmail);

            List<Pet> petList = petService.findPetList(memberId);
            List<PetDto> collect = petList.stream()
                    .map(p -> new PetDto(p.getId(), p.getName(), p.getPetPhotoURLs().get(0))) //Member entity에서 꺼내와 dto에 넣음
                    .collect(Collectors.toList());
            return collect;

        } catch (Exception e) {
            return null;
        }
    }


    //펫 상세 보기
    @GetMapping("/api/pets/{petId}")
    public ResponseEntity<PetInfoResponse>InfoPet(@RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable("petId") Long petId){


        String token = jwtTokenProvider.extractToken(authorizationHeader);
        String memberEmail = jwtTokenProvider.getUserPk(token);

        Member member = memberService.findByEmailLogin(memberEmail);

        Pet pet = petService.findOne(petId);

        if(member == pet.getMember()) {
            PetInfoResponse response = new PetInfoResponse(pet.getName(), pet.getSignificant(), pet.getAnimalType().toString(), pet.getPetPhotoURLs());
            return ResponseEntity.ok(response);
        }
        else {
            PetInfoResponse response = new PetInfoResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
