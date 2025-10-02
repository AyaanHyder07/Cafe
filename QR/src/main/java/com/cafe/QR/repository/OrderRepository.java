package com.cafe.QR.repository;

import com.cafe.QR.entity.Customer;
import com.cafe.QR.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Custom query to find all orders placed by a specific customer
    List<Order> findByCustomer(Customer customer);
}