package com.ecommerce.key_inventory_service.service;

import com.ecommerce.key_inventory_service.dto.KeyInventoryDto;
import com.ecommerce.key_inventory_service.dto.UserKeyDto;
import com.ecommerce.key_inventory_service.entity.KeyHistoryEntity;
import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import com.ecommerce.key_inventory_service.repository.KeyHistoryRepository;
import com.ecommerce.key_inventory_service.repository.KeyInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyInventoryServiceImpl implements KeyInventoryService {
    private final KeyInventoryRepository keyInventoryRepository;
    private final ModelMapper modelMapper;
    private final KeyHistoryRepository keyHistoryRepository;

    @Override
    @Transactional
    public void revokeKeys(String orderId) {
        List<KeyInventoryEntity> keys = keyInventoryRepository.findAllByOrderId(orderId);

        List<KeyInventoryEntity> targets = keys.stream().filter(key -> key.getStatus() == KeyStatus.RESERVED).toList();

        if (targets.isEmpty()) {
            return;
        }

        List<KeyHistoryEntity> histories = targets.stream()
                .map(key -> KeyHistoryEntity.create(
                        key.getId(),
                        orderId,
                        key.getUserId(),
                        KeyStatus.AVAILABLE
                )).toList();

        targets.forEach(KeyInventoryEntity::revoke);

        keyHistoryRepository.saveAll(histories);
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
    public List<KeyInventoryDto> confirmKeys(String orderId) {
        List<KeyInventoryEntity> keys = keyInventoryRepository.findAllByOrderId(orderId);

        if (keys.isEmpty()) {
            throw new RuntimeException("no assigned key for order id : " + orderId);
        }

        keys.stream().filter(key -> key.getStatus() == KeyStatus.RESERVED)
                .forEach(KeyInventoryEntity::confirmSold);

        List<KeyHistoryEntity> histories = keys.stream()
                .map(key -> KeyHistoryEntity.create(
                        key.getId(),
                        key.getOrderId(),
                        key.getUserId(),
                        KeyStatus.SOLD
                )).toList();

        keyHistoryRepository.saveAll(histories);

        return keys.stream().map(key -> modelMapper.map(key, KeyInventoryDto.class)).toList();
    }

    @Override
    public List<UserKeyDto> getAllKeys(String userId) {
        List<KeyInventoryEntity> entities = keyInventoryRepository.findByUserIdOrderBySoldAtDesc(userId, PageRequest.of(0, 10));

        return entities.stream().map(info -> {
            return modelMapper.map(info, UserKeyDto.class);
        }).toList();
    }
}
