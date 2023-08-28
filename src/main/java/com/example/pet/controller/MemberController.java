package com.example.pet.controller;

import com.example.pet.domain.Member;
import com.example.pet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

//    @PostMapping("/main")
//    public String saveMember(@Valid MemberDTO memberDTO, Model model){
//        Member member = new Member();
//        member.setName(memberDTO.getName());
//
//        memberService.join(member);
//        return "main";
//    }

}
