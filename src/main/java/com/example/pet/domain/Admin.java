package com.example.pet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id @GeneratedValue
    @Column(name = "ADMIN_ID")
    private Long id;

    //이름
    @Column(name = "NAME")
    private String name;

    //이메일
    @Column(name = "EMAIL")
    private String email;

    //비밀번호
    @Column(name = "PASSWORD")
    private String pw;
    @Column(name = "PHONE_NUM")
    private String phonenum;

    @Enumerated(EnumType.STRING)
    private final Status status = Status.ADMIN;


    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Member> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Owner> owners = new ArrayList<>();

    public Admin(String name, String email, String pw, String phonenum) {
        this.name = name;
        this.email = email;
        this.pw = pw;
        this.phonenum = phonenum;
    }

}
