package com.act.ldk.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 기본 패스워드 생성
        String password = "admin";
        String encoded = encoder.encode(password);
        
        System.out.println("=== BCrypt Password Generator ===");
        System.out.println();
        System.out.println("Password: " + password);
        System.out.println("Encoded:  " + encoded);
        System.out.println();
        System.out.println("SQL Insert Statement:");
        System.out.println("INSERT INTO member_principal (username, password, enabled, created_date, modified_date)");
        System.out.println("VALUES ('admin', '" + encoded + "', true, NOW(), NOW());");
        System.out.println();
        
        // 여러 패스워드 생성
        System.out.println("=== Multiple Passwords ===");
        String[] passwords = {"admin", "test123", "password123"};
        
        for (String pwd : passwords) {
            String enc = encoder.encode(pwd);
            System.out.println("Password: " + pwd + " -> " + enc);
        }
    }
}