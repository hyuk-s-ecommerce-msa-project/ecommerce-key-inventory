package com.ecommerce.key_inventory_service.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseKey {
    private String productId;
    private String orderId;
    private String userId;
    private String gameKey;
}
