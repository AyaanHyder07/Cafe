package com.cafe.QR.service;

import com.cafe.QR.entity.AdminUser;
import com.cafe.QR.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found: " + username));
        
        // FINAL CHANGE: Hardcode the single, mandatory permission for all admins
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        return new User(
            adminUser.getUsername(), 
            adminUser.getPasswordHash(), 
            Collections.singletonList(authority) 
        );
    }
}