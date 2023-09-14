package com.project.orderservice.controller;

import com.project.orderservice.dto.OrderDto;
import com.project.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@RequestBody OrderDto orderDto) throws Exception {
        try {
            orderService.placeOrder(orderDto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
