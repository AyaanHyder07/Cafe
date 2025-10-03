package com.cafe.QR.config;

import com.cafe.QR.entity.AdminUser;
import com.cafe.QR.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create the first OWNER account if it doesn't exist
        if (adminUserRepository.findByUsername("owner").isEmpty()) {
            AdminUser owner = new AdminUser();
            owner.setUsername("owner");
            owner.setPasswordHash(passwordEncoder.encode("owner_password")); // Use a strong password!
            owner.setRole("OWNER");
            adminUserRepository.save(owner);
            System.out.println(">>> Created default OWNER account <<<");
        }
        
        // You could also create a default STAFF account for testing
        if (adminUserRepository.findByUsername("staff").isEmpty()) {
            AdminUser staff = new AdminUser();
            staff.setUsername("staff");
            staff.setPasswordHash(passwordEncoder.encode("staff_password")); // Use a strong password!
            staff.setRole("STAFF");
            adminUserRepository.save(staff);
            System.out.println(">>> Created default STAFF account <<<");
        }
    }
}