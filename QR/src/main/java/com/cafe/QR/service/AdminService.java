package com.cafe.QR.service;

import com.cafe.QR.dto.AdminUserDTO;
import com.cafe.QR.dto.AuthenticationResponseDTO; // <-- IMPORT THE NEW DTO
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
    private AdminUserRepository adminUserRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // CRITICAL CHANGE: The method now returns an object with the token AND role
    public AuthenticationResponseDTO loginAdmin(AdminUserDTO adminUserDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUserDTO.getUsername(), adminUserDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = adminUserDetailsService.loadUserByUsername(adminUserDTO.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        
        // Extract the role from the userDetails object
        final String role = userDetails.getAuthorities().stream()
                                  .findFirst()
                                  .get().getAuthority().replace("ROLE_", "");

        return new AuthenticationResponseDTO(jwt, role); // <-- Return the new object
    }
    
    public AdminUser registerAdmin(AdminUserDTO adminUserDTO) {
        if (adminUserRepository.findByUsername(adminUserDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }
        
        AdminUser newAdmin = new AdminUser();
        newAdmin.setUsername(adminUserDTO.getUsername());
        newAdmin.setPasswordHash(passwordEncoder.encode(adminUserDTO.getPassword()));
        newAdmin.setRole(adminUserDTO.getRole().toUpperCase()); // Store roles in uppercase
        
        return adminUserRepository.save(newAdmin);
    }
}