package com.cafe.QR.service;

import com.cafe.QR.dto.AdminUserDTO;
import com.cafe.QR.dto.AuthenticationResponseDTO;
import com.cafe.QR.entity.AdminUser;
import com.cafe.QR.repository.AdminUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthenticationResponseDTO loginAdmin(AdminUserDTO adminUserDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(adminUserDTO.getUsername(), adminUserDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract the role from the authenticated user
        String role = authentication.getAuthorities().stream()
                            .findFirst().get().getAuthority().replace("ROLE_", "");

        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setMessage("Login successful");
        
        return response;
    }
    
    public AdminUser registerAdmin(AdminUserDTO adminUserDTO) {
        if (adminUserRepository.findByUsername(adminUserDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + adminUserDTO.getUsername() + "' is already taken!");
        }
        
        AdminUser newAdmin = new AdminUser();
        newAdmin.setUsername(adminUserDTO.getUsername());
        newAdmin.setPasswordHash(passwordEncoder.encode(adminUserDTO.getPassword()));
        
        return adminUserRepository.save(newAdmin);
    }
}