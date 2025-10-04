package com.cafe.QR.service;

import com.cafe.QR.dto.AdminUserDTO;
import com.cafe.QR.dto.AuthenticationResponseDTO;
import com.cafe.QR.entity.AdminUser;
import com.cafe.QR.repository.AdminUserRepository;
import com.cafe.QR.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;
    


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponseDTO loginAdmin(AdminUserDTO adminUserDTO) {
        AdminUser admin = adminUserRepository.findByUsername(adminUserDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(adminUserDTO.getPassword(), admin.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setMessage("Login successful");
        response.setRole(admin.getRole());

        return response;  // <-- Don't forget this!
    }


    public AdminUser registerAdmin(AdminUserDTO adminUserDTO) {
        if (adminUserRepository.findByUsername(adminUserDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }
        
        AdminUser newAdmin = new AdminUser();
        newAdmin.setUsername(adminUserDTO.getUsername());
        newAdmin.setPasswordHash(passwordEncoder.encode(adminUserDTO.getPassword()));
        newAdmin.setRole(adminUserDTO.getRole().toUpperCase());
        
        return adminUserRepository.save(newAdmin);
    }
}