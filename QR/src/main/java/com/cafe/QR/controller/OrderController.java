package com.cafe.QR.controller;

import com.cafe.QR.dto.OrderDTO;
import com.cafe.QR.entity.Order;
import com.cafe.QR.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Public endpoint for customers to place an order.
     */
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Order savedOrder = orderService.placeOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (NoSuchElementException | IllegalStateException e) {
            // Catches errors like "Item not found" or "Item not available"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * SECURED endpoint for admins (staff/owner) to view all orders for the dashboard.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}