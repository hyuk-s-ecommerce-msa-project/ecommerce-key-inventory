package com.ecommerce.key_inventory_service.controller;

import com.ecommerce.key_inventory_service.dto.KeyInventoryDto;
import com.ecommerce.key_inventory_service.dto.UserKeyDto;
import com.ecommerce.key_inventory_service.service.KeyInventoryService;
import com.ecommerce.key_inventory_service.vo.RequestKey;
import com.ecommerce.key_inventory_service.vo.ResponseAllKeys;
import com.ecommerce.key_inventory_service.vo.ResponseKey;
import com.ecommerce.key_inventory_service.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/key-inventory")
public class KeyInventoryController {
    private final KeyInventoryService keyInventoryService;
    private final ModelMapper modelMapper;

    @GetMapping("/keys")
    public ResponseEntity<List<ResponseAllKeys>> getAllKeys() {
        List<ResponseAllKeys> response = keyInventoryService.getAllKeys();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<List<ResponseKey>> assignKey(@RequestBody RequestKey requestKey, @RequestHeader("userId") String userId) {
        List<KeyInventoryDto> keyInventoryDto = keyInventoryService.assignKey(requestKey.getProductId(), requestKey.getOrderId(), userId);

        List<ResponseKey> responseKey = keyInventoryDto.stream().map(
                dto -> modelMapper.map(dto, ResponseKey.class)).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseKey);
    }

    @PostMapping("/confirm")
    public ResponseEntity<List<ResponseKey>> confirmKey(@RequestBody RequestKey requestKey, @RequestHeader("userId") String userId) {
        List<KeyInventoryDto> keyInventoryDto = keyInventoryService.confirmKeys(requestKey.getOrderId(), userId);

        List<ResponseKey> responseKeys = keyInventoryDto.stream().map(
                dto -> modelMapper.map(dto, ResponseKey.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseKeys);
    }

    @GetMapping("/order/key/list")
    public ResponseEntity<List<ResponseUser>> getAllKeysByUser(@RequestHeader("userId") String userId) {
        List<UserKeyDto> userKeyDto = keyInventoryService.getAllKeysByUserId(userId);

        List<ResponseUser> response =  userKeyDto.stream().map(key -> modelMapper.map(key, ResponseUser.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/revoke")
    public ResponseEntity<List<ResponseKey>> revokeKey(@RequestBody RequestKey requestKey, @RequestHeader("userId") String userId) {
        List<KeyInventoryDto> revokedKeys = keyInventoryService.revokeKeys(requestKey.getOrderId(), userId);

        List<ResponseKey> response = revokedKeys.stream().map(key -> modelMapper.map(key, ResponseKey.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
