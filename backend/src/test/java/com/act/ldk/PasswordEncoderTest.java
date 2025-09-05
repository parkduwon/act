package com.act.ldk;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    
    @Test
    public void generateEncodedPasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 테스트용 패스워드들
        String[] passwords = {
            "qkrrudtncjswo##",
        };
        
        System.out.println("=== BCrypt Encoded Passwords ===");
        System.out.println();
        
        for (String password : passwords) {
            String encoded = encoder.encode(password);
            System.out.println("Original: " + password);
            System.out.println("Encoded:  " + encoded);
            System.out.println("Verify:   " + encoder.matches(password, encoded));
            System.out.println("----------------------------------------");
        }
        
        // SQL Insert 문 생성
        System.out.println("\n=== SQL Insert Statements ===");
        System.out.println();
        
        String adminPassword = encoder.encode("admin");
        System.out.println("-- Admin user (password: admin)");
        System.out.println("INSERT INTO member_principal (username, password, enabled, created_date, modified_date)");
        System.out.println("VALUES ('admin', '" + adminPassword + "', true, NOW(), NOW());");
        System.out.println();
        
        String testPassword = encoder.encode("test123");
        System.out.println("-- Test user (password: test123)");
        System.out.println("INSERT INTO member_principal (username, password, enabled, created_date, modified_date)");
        System.out.println("VALUES ('test', '" + testPassword + "', true, NOW(), NOW());");
    }
    
    @Test
    public void verifyPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 기존 인코딩된 패스워드 검증
        String encodedPassword = "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG";
        
        System.out.println("=== Password Verification ===");
        System.out.println("Encoded: " + encodedPassword);
        System.out.println();
        
        String[] testPasswords = {"admin", "admin123", "password", "test123"};
        
        for (String password : testPasswords) {
            boolean matches = encoder.matches(password, encodedPassword);
            System.out.println("Password '" + password + "' matches: " + matches);
        }
    }
    
    @Test
    public void generateSpecificPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== Generate Specific Password ===");
        System.out.print("Enter password to encode: ");
        
        // 직접 원하는 패스워드 입력
        String password = "admin"; // 여기에 원하는 패스워드 입력
        String encoded = encoder.encode(password);
        
        System.out.println("\nPassword: " + password);
        System.out.println("Encoded:  " + encoded);
        System.out.println("\nSQL Insert:");
        System.out.println("INSERT INTO member_principal (username, password, enabled, created_date, modified_date)");
        System.out.println("VALUES ('admin', '" + encoded + "', true, NOW(), NOW());");
    }
}