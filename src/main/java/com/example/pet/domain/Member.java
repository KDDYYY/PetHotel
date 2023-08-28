package com.example.pet.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
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

    //핸드폰 번호
    @Column(name = "PHONE_NUM")
    private String phonenum;

    //주민번호
    @Column(name = "IDENTITY")
    private String identity;

    @Enumerated(EnumType.STRING)
    private final Status status = Status.USER;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;


    @OneToMany(mappedBy = "member")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations = new ArrayList<>();


    public Member(String name, String email, String pw, String phonenum, String identity) {
        this.name = name;
        this.email = email;
        this.pw = pw;
        this.phonenum = phonenum;
        this.identity = identity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

