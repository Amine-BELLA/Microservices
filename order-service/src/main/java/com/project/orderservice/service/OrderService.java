package com.project.orderservice.service;

import com.project.orderservice.dto.OrderDto;
import com.project.orderservice.dto.OrderItemsDto;
import com.project.orderservice.model.Order;
import com.project.orderservice.model.OrderItems;
import com.project.orderservice.model.OrderPlacedEvent;
import com.project.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


    public String placeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderItems> orderItems = orderDto.getOrderItemsDto().stream().map(this::mapToOrderItems).toList();
        order.setOrderItemsList(orderItems);

        //Check if the order is ok in the inventory service  before
        //accepting the order

        Boolean isOrderOk = webClientBuilder.build()
                .post()
                .uri("http://inventory-service/api/inventory/verifyOrder")
                .body(BodyInserters.fromValue(orderItems))
                .retrieve().bodyToMono(Boolean.class).block();

        if (!isOrderOk) {
            return "The ordered products are out of stock!";
        }
        orderRepository.save(order);
        kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
        return "Order placed Successfully!";
    }

    public OrderItems mapToOrderItems(OrderItemsDto orderItemsDto) {
        return OrderItems.builder()
                .skuCode(orderItemsDto.getSkuCode())
                .price(orderItemsDto.getPrice())
                .quantity(orderItemsDto.getQuantity())
                .build();
    }

}
