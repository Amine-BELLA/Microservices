package com.project.orderservice.service;

import com.project.orderservice.dto.OrderDto;
import com.project.orderservice.dto.OrderItemsDto;
import com.project.orderservice.model.Order;
import com.project.orderservice.model.OrderItems;
import com.project.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderDto orderDto) throws Exception{
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderItems> orderItems = orderDto.getOrderItemsDto().stream().map(this::mapToOrderItems).toList();
        order.setOrderItemsList(orderItems);

        //Check if the order is ok in the inventory service  before
        //accepting the order
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082/")
                        .build();
        Boolean isOrderOk = webClient
                .post()
                .uri("api/inventory/verifyOrder")
                .body(BodyInserters.fromValue(orderItems))
                .retrieve().bodyToMono(Boolean.class).block();

        if (!isOrderOk) {
            throw new IllegalArgumentException("The ordered products are out of stock!");
        }
        orderRepository.save(order);
    }

    public OrderItems mapToOrderItems(OrderItemsDto orderItemsDto) {
        return OrderItems.builder()
                .skuCode(orderItemsDto.getSkuCode())
                .price(orderItemsDto.getPrice())
                .quantity(orderItemsDto.getQuantity())
                .build();
    }
}
