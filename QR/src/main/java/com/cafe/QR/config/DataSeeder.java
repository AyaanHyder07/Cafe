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
        if (adminUserRepository.findByUsername("admin").isEmpty()) {
            AdminUser admin = new AdminUser();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            // admin.setRole("ADMIN"); // <-- DELETE THIS LINE
            adminUserRepository.save(admin);
            System.out.println(">>> Created default admin account <<<");
        }
        
        // The day-to-day staff user with limited power
        if (adminUserRepository.findByUsername("staff").isEmpty()) {
            AdminUser staff = new AdminUser();
            staff.setUsername("staff");
            staff.setPasswordHash(passwordEncoder.encode("staff123"));
            adminUserRepository.save(staff);
            System.out.println(">>> Created default STAFF account <<<");
        }
    }
}