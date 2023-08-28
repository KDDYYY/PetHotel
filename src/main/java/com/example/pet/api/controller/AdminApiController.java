package com.example.pet.api.controller;


import com.example.pet.api.ownerDTO.OwnerDto;
import com.example.pet.api.memberDTO.MemberDto;
import com.example.pet.domain.Member;
import com.example.pet.domain.Owner;
import com.example.pet.service.MemberService;
import com.example.pet.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final MemberService memberService;

    private final OwnerService ownerService;

    //모든 사업자 조회
    @GetMapping("/api/admin/owners")
    public List<OwnerDto> findOwners() {
        List<Owner> findowners = ownerService.findOwners();
        List<OwnerDto> collect = findowners.stream()
                .map(o -> new OwnerDto(o.getName(), o.getEmail(), o.getStatus())) //Oner entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }

    //모든 회원 조회
    @GetMapping("/api/admin/members")
    public List<MemberDto> findmembers(){
        List<Member> findmembers = memberService.findMembers();
        List<MemberDto> collect = findmembers.stream()
                .map(m -> new MemberDto(m.getName(), m.getEmail() , m.getStatus())) //Member entity에서 꺼내와 dto에 넣음
                .collect(Collectors.toList());

        return collect;
    }

}
