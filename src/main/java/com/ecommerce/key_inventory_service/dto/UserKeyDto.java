package com.ecommerce.key_inventory_service.dto;

import lombok.Data;

@Data
public class UserKeyDto {
    private String productId;
    private String orderId;
    private String gameKey;
    private String userId;
}
