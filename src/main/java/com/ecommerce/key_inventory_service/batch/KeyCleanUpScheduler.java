package com.ecommerce.key_inventory_service.batch;

import com.ecommerce.key_inventory_service.entity.KeyHistoryEntity;
import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import com.ecommerce.key_inventory_service.repository.KeyHistoryRepository;
import com.ecommerce.key_inventory_service.repository.KeyInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeyCleanUpScheduler {
    private final KeyInventoryRepository keyInventoryRepository;
    private final KeyHistoryRepository keyHistoryRepository;

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void cleanUpKeys() {
        LocalDateTime expiration = LocalDateTime.now().minusMinutes(30);

        List<KeyInventoryEntity> expiredKeys = keyInventoryRepository.findAllByStatusAndSoldAtBefore(KeyStatus.RESERVED, expiration);

        if (expiredKeys.isEmpty()) {
            return;
        }

        expiredKeys.forEach(KeyInventoryEntity::revoke);

        log.info("Number of automatically revoked keys : {}", expiredKeys.size());

        List<KeyHistoryEntity> histories = expiredKeys.stream()
                .map(key -> KeyHistoryEntity.create(
                        key.getId(),
                        key.getOrderId(),
                        key.getUserId(),
                        KeyStatus.AVAILABLE
                )).toList();

        keyHistoryRepository.saveAll(histories);
    }
}
