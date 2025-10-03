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

    /**
     * Public endpoint for an admin/staff/owner to log in.
     * @param adminUserDTO Contains raw username and password.
     * @return ResponseEntity containing the JWT and the user's role on success.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminUserDTO adminUserDTO) {
        try {
            AuthenticationResponseDTO response = adminService.loginAdmin(adminUserDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    /**
     * SECURED endpoint to register a new admin or staff user.
     * Only accessible by an already authenticated user with the 'OWNER' role.
     * @param adminUserDTO Contains details for the new user.
     * @return A success or failure message.
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> registerNewAdmin(@RequestBody AdminUserDTO adminUserDTO) {
        try {
            adminService.registerAdmin(adminUserDTO);
            return ResponseEntity.ok("User registered successfully: " + adminUserDTO.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}