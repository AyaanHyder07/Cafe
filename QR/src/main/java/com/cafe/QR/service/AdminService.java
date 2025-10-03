package com.cafe.QR.service;

import com.cafe.QR.dto.AdminUserDTO;
import com.cafe.QR.dto.AuthenticationResponseDTO;
import com.cafe.QR.entity.AdminUser;
import com.cafe.QR.repository.AdminUserRepository;
import com.cafe.QR.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;
    
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponseDTO loginAdmin(AdminUserDTO adminUserDTO) throws Exception {
        
        final UserDetails userDetails;
        try {
            userDetails = adminUserDetailsService.loadUserByUsername(adminUserDTO.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new Exception("Incorrect username or password", e);
        }
        
        boolean passwordsMatch = passwordEncoder.matches(adminUserDTO.getPassword(), userDetails.getPassword());
        
        if (!passwordsMatch) {
            throw new Exception("Incorrect username or password");
        }
        
        final String jwt = jwtUtil.generateToken(userDetails);
        final String role = userDetails.getAuthorities().stream()
                                  .findFirst()
                                  .orElseThrow(() -> new RuntimeException("User has no roles"))
                                  .getAuthority().replace("ROLE_", "");

        return new AuthenticationResponseDTO(jwt, role);
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