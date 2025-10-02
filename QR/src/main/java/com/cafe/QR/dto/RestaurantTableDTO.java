package com.cafe.QR.dto;

import jakarta.validation.constraints.NotBlank;

public class RestaurantTableDTO {
    
    private Long tableId;

    @NotBlank(message = "Table number cannot be blank")
    private String tableNumber;

    @NotBlank(message = "QR code identifier is required")
    private String qrCode;

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
    
}