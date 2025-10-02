package com.cafe.QR.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemDTO {
    
    private Long orderItemId;
    
    @NotNull
    private Long menuItemId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    private BigDecimal priceAtOrder;
    
    private String notes;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Long menuItemId) {
		this.menuItemId = menuItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPriceAtOrder() {
		return priceAtOrder;
	}

	public void setPriceAtOrder(BigDecimal priceAtOrder) {
		this.priceAtOrder = priceAtOrder;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

    
    
}