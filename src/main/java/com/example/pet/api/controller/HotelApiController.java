package com.example.pet.api.controller;

import com.example.pet.api.hotelDTO.*;
import com.example.pet.domain.*;
import com.example.pet.s3.S3Service;
import com.example.pet.security.JwtTokenProvider;
import com.example.pet.service.HotelService;
import com.example.pet.service.MemberService;
import com.example.pet.service.OwnerService;
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
public class HotelApiController {

    private final MemberService memberService;
    private final HotelService hotelService;

    private final OwnerService ownerService;

    private final JwtTokenProvider jwtTokenProvider;

    private final S3Service s3Service;

    private static final Logger logger = LoggerFactory.getLogger(HotelApiController.class);


    //호텔 등록
    @PostMapping("/api/hotels")
    public ResponseEntity<CreateHotelResponse> saveHotel(@RequestParam("name") String name, @RequestParam("bsNum") String bsNum, @RequestParam("description") String description,
                                                         @RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestPart("imageFiles") List<MultipartFile> imageFiles) { //json 데이터를 hotel로 변환
        try {
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            CreateHotelResponse createHotelResponse = new CreateHotelResponse();
            String ownerEmail = jwtTokenProvider.getUserPk(token);

            Owner owner = ownerService.findByEmailLogin(ownerEmail);

            if (owner.getStatus() == Status.OWNER) {
                Hotel hotel = new Hotel(name, bsNum, description);

                ///이미지 등록부분
                List<String> imageUrls = new ArrayList<>(); //이미지 URL 값
                for(MultipartFile imageFile : imageFiles){
                    String imageUrl = s3Service.saveFile(imageFile);
                    imageUrls.add(imageUrl);
                }
                //-------

                Long id = hotelService.register(hotel, owner, imageUrls);

                createHotelResponse.setHotelId(id);
                createHotelResponse.setError("호텔 등록 성공");

                return ResponseEntity.ok(createHotelResponse);
            } else {
                //잘못된 접근 (사업자가 아닐 경우)
                createHotelResponse.setHotelId(null);
                createHotelResponse.setError("잘못된 접근");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createHotelResponse);
            }
        } catch (Exception e) {
            //logger.error("호텔 등록 실패 - 요청 정보: {}", request, e); // 예외 객체 e와 요청 정보를 로그에 출력
            CreateHotelResponse createHotelResponse = new CreateHotelResponse();

            createHotelResponse.setHotelId(null);
            createHotelResponse.setError("호텔 등록 실패");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createHotelResponse);
        }
    }

    //객실 등록
    @PostMapping("/api/hotel/room/{hotelId}")
    public ResponseEntity<CreateHotelRoomResponse> saveRoom(@RequestBody CreateHotelRoomRequest request,
                                                            @RequestHeader("Authorization") String authorizationHeader,
                                                            @PathVariable("hotelId") Long hotelId){
        try {
            CreateHotelRoomResponse createHotelRoomResponse = new CreateHotelRoomResponse();
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            String ownerEmail = jwtTokenProvider.getUserPk(token);

            Owner owner = ownerService.findByEmailLogin(ownerEmail);
            Hotel hotel = hotelService.findOne(hotelId);

            if(owner == hotel.getOwner()){
                HotelRoom hotelRoom = new HotelRoom(request.getDescription(), request.getFacility(), hotel);
                Long hotelRoomId = hotelService.registerRoom(hotel, hotelRoom);
                createHotelRoomResponse.setHotelRoomId(hotelRoomId);
                createHotelRoomResponse.setError("객실 등록 성공");
                return ResponseEntity.ok(createHotelRoomResponse);

            }else{
                createHotelRoomResponse.setHotelRoomId(null);
                createHotelRoomResponse.setError("잘못된 접근");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createHotelRoomResponse);
            }
        }catch (Exception e){
            CreateHotelRoomResponse createHotelRoomResponse = new CreateHotelRoomResponse();

            createHotelRoomResponse.setHotelRoomId(null);
            createHotelRoomResponse.setError("객실 등록 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createHotelRoomResponse);
        }
    }


    //객실 목록
    @GetMapping("/api/hotel/room/{hotelId}")
    public List<HotelRoomDto> findHotelRooms(@PathVariable("hotelId") Long hotelId){

        List<HotelRoom> findHotelRooms = hotelService.findHotelRoomList(hotelId);
        List<HotelRoomDto> collect = findHotelRooms.stream()
                .map(h -> new HotelRoomDto(h.getId(), h.getDescription(), h.getFacility())) //Member entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }


    //객실 조회
    @GetMapping("/api/hotel/room/{hotelRoomId}")
    public ResponseEntity<HotelRoomInfoResponse> InfoHotelRoom(@PathVariable("hotelRoomId") Long hotelRoomId){
        HotelRoom hotelRoom = hotelService.findRoom(hotelRoomId);

        if(hotelRoom == null ){
            return ResponseEntity.notFound().build();
        }else{
            HotelRoomInfoResponse hotelRoomInfoResponse = new HotelRoomInfoResponse(hotelRoom.getDescription(), hotelRoom.getFacility());
            return ResponseEntity.ok(hotelRoomInfoResponse);
        }
    }




//    //호텔 등록
//    @PostMapping("/api/hotel")
//    public ResponseEntity<CreateHotelResponse> saveHotel(CreateHotelRequest request, //@ModelAttribute 생략 가능
//                                                         @RequestHeader("Authorization") String authorizationHeader) { //json 데이터를 hotel로 변환
//        try {
//            String token = jwtTokenProvider.extractToken(authorizationHeader);
//            CreateHotelResponse createHotelResponse = new CreateHotelResponse();
//            String ownerEmail = jwtTokenProvider.getUserPk(token);
//
//            Owner owner = ownerService.findByEmailLogin(ownerEmail);
//
//            if (owner.getStatus() == Status.OWNER) {
//                Hotel hotel = new Hotel(request.getName(), request.getBsNum(), request.getDescription());
//
//                ///이미지 등록부분
//                List<String> imageUrls = new ArrayList<>(); //이미지 URL 값
//                for(MultipartFile imageFile : request.getImageFiles()){
//                    String imageUrl = s3Service.saveFile(imageFile);
//                    imageUrls.add(imageUrl);
//                }
//                //-------
//
//                Long id = hotelService.register(hotel, owner, imageUrls);
//
//                createHotelResponse.setId(id);
//                createHotelResponse.setError("호텔 등록 성공");
//
//                return ResponseEntity.ok(createHotelResponse);
//            } else {
//                //잘못된 접근 (사업자가 아닐 경우)
//                createHotelResponse.setId(null);
//                createHotelResponse.setError("잘못된 접근");
//
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createHotelResponse);
//            }
//        } catch (Exception e) {
//            //logger.error("호텔 등록 실패 - 요청 정보: {}", request, e); // 예외 객체 e와 요청 정보를 로그에 출력
//            CreateHotelResponse createHotelResponse = new CreateHotelResponse();
//
//            createHotelResponse.setId(null);
//            createHotelResponse.setError("호텔 등록 실패");
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createHotelResponse);
//        }
//    }

    //수정
    @PostMapping("/api/hotels/{hotelId}")
    public ResponseEntity<ModifyResponse> modifyHotel(@RequestParam("name") String name, @RequestParam("bsNum") String bsNum,
                                                      @RequestParam("description") String description,
                                                      @RequestPart("imageFiles") List<MultipartFile> imageFiles,
                                                      @RequestHeader("Authorization") String authorizationHeader,
                                                      @PathVariable("hotelId") Long hotelId){
        try {
            ModifyResponse modifyResponse = new ModifyResponse();

            String token = jwtTokenProvider.extractToken(authorizationHeader);
            String ownerEmail = jwtTokenProvider.getUserPk(token);

            Owner owner = ownerService.findByEmailLogin(ownerEmail);
            Hotel hotel = hotelService.findOne(hotelId);

            if(owner != hotel.getOwner()){
                modifyResponse.setHotelId(null);
                modifyResponse.setError("잘못된 접근");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(modifyResponse);
            }else {
                hotelService.modify(hotelId);

                Hotel modifyHotel = new Hotel(name, bsNum, description);

                ///이미지 등록부분
                List<String> imageUrls = new ArrayList<>(); //이미지 URL 값
                for (MultipartFile imageFile : imageFiles) {
                    String imageUrl = s3Service.saveFile(imageFile);
                    imageUrls.add(imageUrl);
                }
                //-------

                Long modifyId = hotelService.register(modifyHotel, owner, imageUrls);

                modifyResponse.setHotelId(modifyId);
                modifyResponse.setError("호텔 수정 성공");

                return ResponseEntity.ok(modifyResponse);
            }
        } catch (Exception e) {
            logger.error("호텔 수정 실패 - 요청 정보: {}", e); // 예외 객체 e와 요청 정보를 로그에 출력
            ModifyResponse modifyResponse = new ModifyResponse();

            modifyResponse.setHotelId(null);
            modifyResponse.setError("호텔 수정 실패");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(modifyResponse);
        }
    }


    //모든 호텔 리스트
    @GetMapping("/api/hotels")
    public List<HotelDto> findHotels(){
        List<Hotel> findHotels = hotelService.findHotels();
        List<HotelDto> collect = findHotels.stream()
                .map(h -> new HotelDto(h.getId(), h.getName(), h.getHotelPhotoURLs().get(0))) //Member entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }

    //호텔 조회
    @GetMapping("/api/hotel/{hotelId}")
    public ResponseEntity<HotelInfoResponse> InfoHotel(@PathVariable("hotelId") Long hotelId){
        Hotel hotel = hotelService.findOne(hotelId);

        if(hotel == null){
            return ResponseEntity.notFound().build();
        }

        Owner owner = hotel.getOwner(); //호텔 주인

        HotelInfoResponse response = new HotelInfoResponse(hotel.getName(), hotel.getDescription(), owner.getPhoneNum(), hotel.getHotelPhotoURLs());
        return ResponseEntity.ok(response);
    }

    //사업자가 등록한 호텔
    @GetMapping("/api/hotel/{ownerId}")
    public List<HotelDto> findMyHotels(@RequestHeader("Authorization") String authorizationHeader,
                                       @PathVariable("ownerId") Long ownerId){
        try {
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            String ownerEmail = jwtTokenProvider.getUserPk(token);
            Owner owner = ownerService.findByEmailLogin(ownerEmail);

            List<Hotel> hotelList = hotelService.findHotelList(ownerId);
            //List<Hotel> hotelList = owner.getHotels();
            List<HotelDto> collect = hotelList.stream()
                    .map(h -> new HotelDto(h.getId(), h.getName(), h.getHotelPhotoURLs().get(0))) //Member entity에서 꺼내와 dto에 넣음
                    .collect(Collectors.toList());

            return collect;
        }catch (Exception e){
            return null;
        }
    }


    //예약
    @PostMapping("/api/hotel/res/{hotelId}")
    public ResponseEntity<ReservationResponse> reservationHotel(@PathVariable("hotelId")Long hotelId,
                                                                @RequestBody ReservationRequest request,
                                                                @RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = jwtTokenProvider.extractToken(authorizationHeader);
            String memberEmail = jwtTokenProvider.getUserPk(token);

            Member member = memberService.findByEmailLogin(memberEmail);
            Hotel hotel = hotelService.findOne(hotelId);

            if(!hotel.isAvailable(request.getStartTime(), request.getEndTime())){
                ReservationResponse response = new ReservationResponse(null,"예약 실패");
                return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response); //예약 불가

            }else { //예약 가능
                Long resId = hotelService.createReservation(hotel, member, request.getStartTime(), request.getEndTime());

                ReservationResponse response = new ReservationResponse(resId,"예약 성공");
                return ResponseEntity.ok(response);
            }
        }catch (Exception e){
            ReservationResponse response = new ReservationResponse(null,"예약 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }







}






















