package com.project.inventoryservice.service;


import com.project.inventoryservice.model.Inventory;
import com.project.inventoryservice.repository.InventoryRepository;
import com.project.inventoryservice.sdk.Order;
import com.project.inventoryservice.sdk.OrderItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Boolean isInStock(List<String> skuCodes) {
        if (skuCodes.isEmpty()) {
            throw new IllegalArgumentException("List of products cannot be null");
        }

        for (int i=0; i < skuCodes.size(); i ++) {
            Optional<Inventory> optInventory = inventoryRepository.findBySkuCode(skuCodes.get(i));
            if (!optInventory.isPresent()) {
                throw new IllegalArgumentException("Product is out of stock");
            }
        }

        return true;
    }

    public Boolean isOrderOk(List<OrderItems> orders) {
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("Order cannot be Empty!");
        }

        for (int i=0; i <orders.size(); i++) {
            OrderItems order = orders.get(i);
            String skuCode = order.getSkuCode();
            Integer quantity = order.getQuantity();

            Optional<Inventory> optionalInventory = inventoryRepository.findBySkuCode(skuCode);
            if (!optionalInventory.isPresent()) {
                throw new IllegalArgumentException("Product is out of stock");
            }
            Inventory inventory = optionalInventory.get();
            Integer inventoryQuantity = inventory.getQuantity();
            if (inventoryQuantity < quantity) {
                throw new IllegalArgumentException("Product out of stock");
            }
        }

        return true;
    }
}
