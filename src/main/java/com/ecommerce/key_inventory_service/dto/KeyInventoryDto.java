package com.ecommerce.key_inventory_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class KeyInventoryDto {
    private String productId;
    private String orderId;
    private String gameKey;
    private String userId;
}
