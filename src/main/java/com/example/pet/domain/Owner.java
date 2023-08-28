package com.example.pet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "OWNER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    //이메일
    @Column(name = "EMAIL")
    private String email;

    //비밀번호
    @Column(name = "PASSWORD")
    private String pw;

    //핸드폰 번호
    @Column(name = "PHONENUM")
    private String phoneNum;

    //주민번호
    @Column(name = "IDENTITY")
    private String identity;

    //가입 상태(Owner)
    @Enumerated(EnumType.STRING)
    private final Status status = Status.OWNER;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "owner")
    private List<Hotel> hotels = new ArrayList<>();

    public Owner(String name, String email, String pw, String phoneNum, String identity) {
        this.name = name;
        this.email = email;
        this.pw = pw;
        this.phoneNum = phoneNum;
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
