package com.cafe.QR.repository;

import com.cafe.QR.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Custom query to find a customer by their phone number
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}