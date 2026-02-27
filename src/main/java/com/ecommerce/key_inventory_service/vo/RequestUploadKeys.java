package com.ecommerce.key_inventory_service.vo;

import lombok.Data;

@Data
public class RequestUploadKeys {
    private String productId;
    private String gameKey;
}
