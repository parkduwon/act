package com.act.ldk.controller;

import com.act.ldk.common.security.JwtUtil;
import com.act.ldk.controller.dto.LoginRequest;
import com.act.ldk.domain.entity.MemberPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            MemberPrincipal user = (MemberPrincipal) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user.getUsername());
            
            // JWT를 HttpOnly 쿠키로 설정
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // HTTPS 환경에서는 true로 설정
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 24시간
            response.addCookie(cookie);
            
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("username", user.getUsername());
            
            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
    
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        // JwtAuthenticationFilter에서 이미 인증을 확인했으므로 여기까지 도달하면 인증된 것
        return ResponseEntity.ok(Map.of("authenticated", true));
    }
}