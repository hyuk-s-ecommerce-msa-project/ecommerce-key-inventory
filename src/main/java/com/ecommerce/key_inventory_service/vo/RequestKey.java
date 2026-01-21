package com.ecommerce.key_inventory_service.vo;

import lombok.Data;

import java.util.List;

@Data
public class RequestKey {
    private List<String> productId;
    private String orderId;
}
