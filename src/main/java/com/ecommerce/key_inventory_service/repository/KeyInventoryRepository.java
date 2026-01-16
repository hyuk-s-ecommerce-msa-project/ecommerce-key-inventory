package com.ecommerce.key_inventory_service.repository;

import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface KeyInventoryRepository extends CrudRepository<KeyInventoryEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    KeyInventoryEntity findFirstByProductIdAndStatus(String productId, KeyStatus status);

    List<KeyInventoryEntity> findByUserIdOrderBySoldAtDesc(String userId, Pageable pageable);
    List<KeyInventoryEntity> findAllByOrderId(String orderId);

    List<KeyInventoryEntity> findAllByStatusAndSoldAtBefore(KeyStatus status, LocalDateTime soldAt);
}
