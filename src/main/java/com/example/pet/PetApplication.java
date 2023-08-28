package com.example.pet;

import com.example.pet.domain.Member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

import javax.persistence.EntityManager;

@SpringBootApplication
public class PetApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetApplication.class, args);
    }

}
