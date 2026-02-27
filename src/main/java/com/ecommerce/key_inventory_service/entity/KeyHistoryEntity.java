package com.ecommerce.key_inventory_service.entity;

import com.ecommerce.key_inventory_service.entity.enums.KeyStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "key_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class KeyHistoryEntity {
    @Id
    private Long id;

    private Long keyId;
    private String orderId;
    private String userId;
    @Enumerated(EnumType.STRING)
    private KeyStatus status;
    @CreatedDate
    private LocalDateTime createdDate;

    public static KeyHistoryEntity create(Long id, Long keyId, String orderId, String userId, KeyStatus status) {
        KeyHistoryEntity keyHistoryEntity = new KeyHistoryEntity();

        keyHistoryEntity.id = id;
        keyHistoryEntity.keyId = keyId;
        keyHistoryEntity.orderId = orderId;
        keyHistoryEntity.userId = userId;
        keyHistoryEntity.status = status;

        return keyHistoryEntity;
    }
}
