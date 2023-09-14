package com.project.inventoryservice.controller;

import com.project.inventoryservice.model.Inventory;
import com.project.inventoryservice.sdk.Order;
import com.project.inventoryservice.sdk.OrderItems;
import com.project.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@RequestParam("sku-codes") List<String> skuCodes) {
        return inventoryService.isInStock(skuCodes);
    }

    @PostMapping("/verifyOrder")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isOrderOk(@RequestBody List<OrderItems> orders) {
        return inventoryService.isOrderOk(orders);
    }
}
