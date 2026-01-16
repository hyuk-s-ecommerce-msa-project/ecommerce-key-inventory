package com.ecommerce.key_inventory_service.service;

import com.ecommerce.key_inventory_service.dto.KeyInventoryDto;
import com.ecommerce.key_inventory_service.dto.UserKeyDto;

import java.util.List;

public interface KeyInventoryService {
    List<KeyInventoryDto> assignKey(List<String> productId, String orderId, String userId);
    List<KeyInventoryDto> confirmKeys(String orderId);
    List<UserKeyDto> getAllKeys(String userId);
    void revokeKeys(String orderId);
}
