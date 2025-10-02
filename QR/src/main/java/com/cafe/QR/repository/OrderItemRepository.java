package com.cafe.QR.repository;

import com.cafe.QR.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Basic CRUD operations are usually sufficient for this entity
}