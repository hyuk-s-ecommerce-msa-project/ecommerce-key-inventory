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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "key_inventory")
@EntityListeners(AuditingEntityListener.class)
public class KeyInventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productId;
    @Column(unique = true, nullable = false)
    private String gameKey;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeyStatus status;
    @Column(nullable = true)
    private String orderId;
    @Column(nullable = true)
    private String userId;
    @CreatedDate
    private LocalDateTime keyUploadedDate;
    @Column(nullable = true)
    private LocalDateTime soldAt;

    public void assignToOrder(String orderId, String userId) {
        this.status = KeyStatus.RESERVED;
        this.orderId = orderId;
        this.userId = userId;
    }

    public void revoke() {
        this.status = KeyStatus.AVAILABLE;
        this.orderId = null;
        this.userId = null;
        this.soldAt = null;
    }

    public void confirmSold() {
        this.status = KeyStatus.SOLD;
        this.soldAt = LocalDateTime.now();
    }
}
