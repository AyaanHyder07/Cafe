package com.cafe.QR.dto;

public class AuthenticationResponseDTO {

    private final String jwt;
    private final String role;

    public AuthenticationResponseDTO(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRole() {
        return role;
    }
}