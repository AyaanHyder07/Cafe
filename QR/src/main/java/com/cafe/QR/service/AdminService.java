package com.cafe.QR.service;

import com.cafe.QR.dto.AdminUserDTO;
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

    /**
     * Business Logic: Authenticates an admin user and generates a JWT.
     * This is more than just fetching a user; it validates credentials against
     * the stored password hash.
     *
     * @param adminUserDTO DTO containing the raw username and password.
     * @return A JWT string if authentication is successful.
     * @throws Exception if credentials are invalid.
     */
    public String loginAdmin(AdminUserDTO adminUserDTO) throws Exception {
        try {
            // Spring Security handles the password hash comparison here
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUserDTO.getUsername(), adminUserDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = adminUserDetailsService.loadUserByUsername(adminUserDTO.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
    
    /**
     * Business Logic: Registers a new admin user.
     * It ensures the username is unique and securely hashes the password before saving.
     *
     * @param adminUserDTO DTO containing the new admin's details.
     * @return The newly created AdminUser entity.
     */
    public AdminUser registerAdmin(AdminUserDTO adminUserDTO) {
        if (adminUserRepository.findByUsername(adminUserDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }
        
        AdminUser newAdmin = new AdminUser();
        newAdmin.setUsername(adminUserDTO.getUsername());
        // CRITICAL: Hash the password before storing it in the database
        newAdmin.setPasswordHash(passwordEncoder.encode(adminUserDTO.getPassword()));
        newAdmin.setRole(adminUserDTO.getRole());
        
        return adminUserRepository.save(newAdmin);
    }
}