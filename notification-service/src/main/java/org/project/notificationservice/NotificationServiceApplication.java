package org.project.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.project.notificationservice.event.OrderPlacedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handlePlacingOrder(OrderPlacedEvent orderPlacedEvent) {
        log.info("Order placed: " + orderPlacedEvent.getOrderNumber());
    }
}
