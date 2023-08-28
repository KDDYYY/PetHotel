package com.example.pet.controller;

import com.example.pet.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadImages(@RequestPart("imageFiles") List<MultipartFile> imageFiles) {
        try {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                String imageUrl = s3Service.saveFile(imageFile);
                imageUrls.add(imageUrl);
            }
            return ResponseEntity.ok("Images uploaded successfully. URLs: " + imageUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed.");
        }
    }

//    @GetMapping("/{imageName}")
//    public ResponseEntity<String> getImage(@PathVariable String imageName) {
//        try {
//            String imageUrl = s3Service.getImageUrl(imageName);
//
//            if (imageUrl != null) {
//                return ResponseEntity.ok(imageUrl);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


    @GetMapping("/{imageNames}")
    public ResponseEntity<List<String>> getImages(@PathVariable List<String> imageNames) {
        try {
            List<String> imageUrls = new ArrayList<>();
            for (String imageName : imageNames) {
                String imageUrl = s3Service.getImageUrl(imageName);
                if (imageUrl != null) {
                    imageUrls.add(imageUrl);
                }
            }
            if (!imageUrls.isEmpty()) {
                return ResponseEntity.ok(imageUrls);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}