package com.cafe.QR.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AdminUserDTO {

    private Long adminId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;
    
    // This field is used for creating/updating passwords. It should be null in responses.
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password; 


    private LocalDateTime createdAt;
    
    // --- Constructors ---

    public AdminUserDTO() {
    }

    public AdminUserDTO(Long adminId, String username, LocalDateTime createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.createdAt = createdAt;
    }


    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}