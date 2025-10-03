package com.cafe.QR.service;

import com.cafe.QR.dto.OrderDTO;
import org.springframework.data.domain.Sort;
import com.cafe.QR.dto.OrderItemDTO;
import com.cafe.QR.entity.*;
import com.cafe.QR.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    /**
     * --- CORE BUSINESS LOGIC ---
     * This method handles the entire process of placing an order.
     * It's marked as @Transactional, meaning if any step fails, all database
     * changes in this method will be rolled back, preventing partial/corrupt orders.
     *
     * The logic performs these key steps:
     * 1. Validates the existence of the Customer and Table.
     * 2. Iterates through each item in the order request.
     * 3. For each item, it validates its existence and availability.
     * 4. It captures the item's current price (`priceAtOrder`).
     * 5. It calculates the total amount for the entire order.
     * 6. It saves the Order and all its associated OrderItems to the database.
     *
     * @param orderDTO The DTO containing all order details from the frontend.
     * @return The newly created and saved Order entity.
     */
    @Transactional
    public Order placeOrder(OrderDTO orderDTO) {
        // Step 1: Validate Customer and Table
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + orderDTO.getCustomerId()));
        
        RestaurantTable table = restaurantTableRepository.findById(orderDTO.getTableId())
                .orElseThrow(() -> new NoSuchElementException("Table not found with ID: " + orderDTO.getTableId()));

        // Step 2: Prepare the Order and initialize total amount
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setTable(table);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItemsList = new ArrayList<>();

        // Step 3 & 4: Process each OrderItemDTO
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new NoSuchElementException("Menu Item not found with ID: " + itemDTO.getMenuItemId()));

            // Business Rule: Cannot order an item that is not available
            if (!menuItem.isAvailable()) {
                throw new IllegalStateException("Cannot order item '" + menuItem.getName() + "' as it is not available.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            
            // Business Rule: Capture the price at the time of order
            orderItem.setPriceAtOrder(menuItem.getPrice()); 
            
            orderItemsList.add(orderItem);

            // Step 5: Add to the total amount calculation
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // Finalize and save the order
        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderItems(orderItemsList);
        
        // Because of CascadeType.ALL on the Order entity's 'orderItems' field,
        // saving the order will also automatically save all the associated order items.
        return orderRepository.save(newOrder);
    }
    
    /**
     * Retrieves all orders, sorted by the most recent first.
     * @return A sorted list of all orders.
     */
    public List<Order> getAllOrders() {
        // CRITICAL CHANGE: Sort orders by creation date in descending order.
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}