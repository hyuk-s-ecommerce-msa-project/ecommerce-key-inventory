package com.ecommerce.key_inventory_service.service;

import com.ecommerce.key_inventory_service.dto.KeyInventoryDto;
import com.ecommerce.key_inventory_service.dto.UserKeyDto;
import com.ecommerce.key_inventory_service.entity.KeyHistoryEntity;
import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import com.ecommerce.key_inventory_service.repository.KeyHistoryRepository;
import com.ecommerce.key_inventory_service.repository.KeyInventoryRepository;
import com.ecommerce.key_inventory_service.vo.RequestKey;
import com.ecommerce.key_inventory_service.vo.ResponseAllKeys;
import com.ecommerce.key_inventory_service.vo.ResponseKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyInventoryServiceImpl implements KeyInventoryService {
    private final KeyInventoryRepository keyInventoryRepository;
    private final ModelMapper modelMapper;
    private final KeyHistoryRepository keyHistoryRepository;

    @Override
    public List<ResponseAllKeys> getAllKeys() {
        List<KeyInventoryEntity> allKeys = keyInventoryRepository.findAll();

        if (allKeys.isEmpty()) {
            throw new RuntimeException("there is no such key in inventory");
        }

        return allKeys.stream().map(keys -> modelMapper.map(keys, ResponseAllKeys.class)).toList();
    }

    @Override
    @Transactional
    public List<KeyInventoryDto> revokeKeys(String orderId, String userId) {
        List<KeyInventoryEntity> keys = keyInventoryRepository.findAllByOrderId(orderId);

        if (!keys.isEmpty() && !keys.get(0).getUserId().equals(userId)) {
            throw new RuntimeException("Access denied for this order");
        }

        List<KeyInventoryEntity> targets = keys.stream().filter(key -> key.getStatus() == KeyStatus.RESERVED).toList();

        if (targets.isEmpty()) {
            throw new RuntimeException("No such key");
        }

        List<KeyHistoryEntity> histories = targets.stream()
                .map(key -> KeyHistoryEntity.create(
                        key.getId(),
                        orderId,
                        userId,
                        KeyStatus.AVAILABLE
                )).toList();

        targets.forEach(KeyInventoryEntity::revoke);

        keyHistoryRepository.saveAll(histories);

        return targets.stream().map(key -> modelMapper.map(key, KeyInventoryDto.class)).toList();
    }

    @Override
    @Transactional
    public List<KeyInventoryDto> assignKey(List<String> productIds, String orderId, String userId) {
        return productIds.stream().map(productId -> {
            KeyInventoryEntity key = keyInventoryRepository.findFirstByProductIdAndStatus(productId, KeyStatus.AVAILABLE);

            if (key == null) {
                throw new RuntimeException("no stock for product id : " + productId);
            }

            key.assignToOrder(orderId, userId);

            return modelMapper.map(key, KeyInventoryDto.class);
        }).toList();
    }

    @Override
    @Transactional
    public List<KeyInventoryDto> confirmKeys(String orderId, String userId) {
        List<KeyInventoryEntity> keys = keyInventoryRepository.findAllByOrderId(orderId);

        if (keys.isEmpty()) {
            throw new RuntimeException("no assigned key for order id : " + orderId);
        }

        if (!keys.get(0).getUserId().equals(userId)) {
            throw new RuntimeException("Access denied for this order");
        }

        keys.stream().filter(key -> key.getStatus() == KeyStatus.RESERVED)
                .forEach(KeyInventoryEntity::confirmSold);

        List<KeyHistoryEntity> histories = keys.stream()
                .map(key -> KeyHistoryEntity.create(
                        key.getId(),
                        key.getOrderId(),
                        userId,
                        KeyStatus.SOLD
                )).toList();

        keyHistoryRepository.saveAll(histories);

        return keys.stream().map(key -> modelMapper.map(key, KeyInventoryDto.class)).toList();
    }

    @Override
    public List<UserKeyDto> getAllKeysByUserId(String userId) {
        List<KeyInventoryEntity> entities = keyInventoryRepository.findByUserIdOrderBySoldAtDesc(userId, PageRequest.of(0, 10));

        return entities.stream().map(info -> {
            return modelMapper.map(info, UserKeyDto.class);
        }).toList();
    }
}
