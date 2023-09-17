package com.project.orderservice.controller;

import com.project.orderservice.dto.OrderDto;
import com.project.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "placeOrderFallBack")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderDto orderDto) {
            return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderDto));
    }

    public CompletableFuture<String>  placeOrderFallBack(OrderDto orderDto, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "System Error, please retry later!");
    }
}
