package com.ecommerce.key_inventory_service.service;

import com.ecommerce.key_inventory_service.dto.KeyInventoryDto;
import com.ecommerce.key_inventory_service.dto.UserKeyDto;
import com.ecommerce.key_inventory_service.vo.RequestKey;
import com.ecommerce.key_inventory_service.vo.ResponseAllKeys;
import com.ecommerce.key_inventory_service.vo.ResponseKey;

import java.util.List;

public interface KeyInventoryService {
    List<KeyInventoryDto> assignKey(List<String> productId, String orderId, String userId);
    List<KeyInventoryDto> confirmKeys(String orderId, String userId);
    List<UserKeyDto> getAllKeysByUserId(String userId);
    void revokeKeys(String orderId, String userId);
    List<ResponseAllKeys> getAllKeys();
}
