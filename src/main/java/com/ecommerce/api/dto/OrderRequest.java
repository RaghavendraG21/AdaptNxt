package com.ecommerce.api.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemDTO> items;
    private String shippingAddress;
    private String paymentMethod;

    // Getters and Setters
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
