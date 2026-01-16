package com.ecommerce.key_inventory_service.vo;

import lombok.Data;

@Data
public class ResponseUser {
    private String productId;
    private String orderId;
    private String gameKey;
    private String userId;
}
