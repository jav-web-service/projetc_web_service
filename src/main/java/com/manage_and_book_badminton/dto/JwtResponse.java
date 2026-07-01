package com.manage_and_book_badminton.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    
    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.type = "Bearer";
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
