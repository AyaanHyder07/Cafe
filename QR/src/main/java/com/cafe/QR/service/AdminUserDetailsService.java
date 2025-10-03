package com.cafe.QR.service;

import com.cafe.QR.entity.AdminUser; // Your entity class
import com.cafe.QR.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <-- IMPORT THIS
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections; // <-- IMPORT THIS

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found with username: " + username));

        // CRITICAL CHANGE: We now create a "GrantedAuthority" from the role string in our database.
        // Spring Security needs the "ROLE_" prefix to correctly identify it as a role.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + adminUser.getRole());

        return new User(
            adminUser.getUsername(), 
            adminUser.getPasswordHash(), 
            Collections.singletonList(authority) // <-- Pass the role to Spring Security
        );
    }
}