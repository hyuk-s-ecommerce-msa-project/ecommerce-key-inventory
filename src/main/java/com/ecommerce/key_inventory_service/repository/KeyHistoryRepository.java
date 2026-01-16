package com.ecommerce.key_inventory_service.repository;

import com.ecommerce.key_inventory_service.entity.KeyHistoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface KeyHistoryRepository extends CrudRepository<KeyHistoryEntity,Long> {
}
