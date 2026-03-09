package com.ecommerce.key_inventory_service.service;

import com.ecommerce.key_inventory_service.entity.KeyInventoryEntity;
import com.ecommerce.key_inventory_service.repository.KeyInventoryRepository;
import com.ecommerce.snowflake.util.SnowflakeIdGenerator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeyInventoryServiceImplTest {
    @Autowired
    private KeyInventoryRepository keyInventoryRepository;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    void load700kKeysWithFactoryMethod() {
        int totalCount = 700_000;
        int batchSize = 1000;

        for (int i = 1; i <= totalCount; i++) {
            KeyInventoryEntity entity = KeyInventoryEntity.createKey(
                    snowflakeIdGenerator.nextId(),
                    "3542350",
                    generateGameKey()
            );

            em.persist(entity);

            if (i % batchSize == 0) {
                em.flush();
                em.clear(); // 여기서 1차 캐시를 완전히 비워야 메모리가 회수됩니다.
            }
        }
        em.flush();
        System.out.println("최종 700,000건 삽입 완료!");
    }

    private String generateGameKey() {
        // aaaa-bbbb-cccc-dddd 형식 생성
        return String.format("%s-%s-%s-%s",
                UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4),
                UUID.randomUUID().toString().substring(0, 4)).toUpperCase();
    }
}