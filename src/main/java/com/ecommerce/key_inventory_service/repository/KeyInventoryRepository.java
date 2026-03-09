package com.ecommerce.key_inventory_service.repository;

import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface KeyInventoryRepository extends CrudRepository<KeyInventoryEntity, Long> {
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<KeyInventoryEntity> findFirstByProductIdAndStatus(String productId, KeyStatus status, Pageable pageable);

    List<KeyInventoryEntity> findByUserIdOrderBySoldAtDesc(String userId, Pageable pageable);
    List<KeyInventoryEntity> findAllByOrderId(String orderId);

    List<KeyInventoryEntity> findAllByStatusAndSoldAtBefore(KeyStatus status, LocalDateTime soldAt);

    @Query(value = "SELECT * FROM key_inventory k " +
            "WHERE k.product_id = :productId AND k.status = 'AVAILABLE' " +
            "LIMIT :count FOR UPDATE WAIT 1", nativeQuery = true)
    List<KeyInventoryEntity> findAvailableKeys(String productId, int count);

    @Override
    List<KeyInventoryEntity> findAll();
}
