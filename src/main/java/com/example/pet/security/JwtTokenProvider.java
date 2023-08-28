package com.example.pet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenProvider {
    private Key secretKey;

    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    private final UserDetailsService userDetailsService;



    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // JWT 토큰 생성
    //public String createToken(String userPk, List<String> roles)
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
       // claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    //토큰 무효화
//    public boolean invalidateToken(String token) {
//        try {
//            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//
//            // 현재 시간으로 설정하여 토큰의 만료 시간을 조정
//            Date now = new Date();
//            claims.setExpiration(now);
//
//            // 새 토큰 생성 없이 원본 토큰의 만료 시간 조정만 수행
//            Jwts.builder()
//                    .setClaims(claims)
//                    .signWith(SignatureAlgorithm.HS256, secretKey)
//                    .compact();
//
//            // 토큰 무효화 및 조정이 성공했을 경우 true 반환
//            return true;
//        } catch (Exception e) {
//            // 토큰 무효화 및 조정 중에 오류가 발생한 경우에 대한 처리
//            // 예: 예외 발생 시 false 반환
//            return false;
//        }
//    }


    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


    private Set<String> invalidatedTokens = new HashSet<>();

    // invalidateToken 메서드
    public boolean invalidateToken(String token) {
        try {
            // 무효화된 토큰을 invalidatedTokens 목록에 추가
            invalidatedTokens.add(token);

            // 토큰 무효화 성공
            return true;
        } catch (Exception e) {
            // 토큰 무효화 중에 오류가 발생한 경우에 대한 처리
            return false;
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    // validateToken 메서드
    public boolean validateToken(String jwtToken) {
        // invalidatedTokens 목록에서 토큰이 무효화되었는지 확인
        if (invalidatedTokens.contains(jwtToken)) {
            return false;
        }
        try {
            // 토큰의 클레임을 파싱하여 만료 시간을 확인합니다.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            // 토큰 파싱 중에 오류가 발생한 경우에 대한 처리
            return false;
        }
    }

    // extractToken 메서드
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


}