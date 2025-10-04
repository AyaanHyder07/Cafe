package com.cafe.QR.controller;

import com.cafe.QR.dto.AdminUserDTO;
import com.cafe.QR.dto.AuthenticationResponseDTO;
import com.cafe.QR.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminUserDTO adminUserDTO) {
        try {
            AuthenticationResponseDTO response = adminService.loginAdmin(adminUserDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // This will now only be triggered by genuinely bad credentials
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewAdmin(@RequestBody AdminUserDTO adminUserDTO) {
        try {
            adminService.registerAdmin(adminUserDTO);
            return ResponseEntity.ok("User registered successfully: " + adminUserDTO.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}