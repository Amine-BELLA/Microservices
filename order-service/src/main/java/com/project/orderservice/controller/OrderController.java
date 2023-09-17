package com.project.orderservice.controller;

import com.project.orderservice.dto.OrderDto;
import com.project.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name="inventory", fallbackMethod = "placeOrderFallBack")
    public String placeOrder(@RequestBody OrderDto orderDto) throws Exception {
        try {
            orderService.placeOrder(orderDto);
            return "Order placed successfully!";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String placeOrderFallBack(OrderDto orderDto, RuntimeException runtimeException) {
        return "System Error, please retry later!";
    }
}
