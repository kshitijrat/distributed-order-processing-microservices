package com.project.orderservice.order.service;

import com.project.orderservice.order.entity.Order;
import com.project.orderservice.order.entity.OrderStatus;
import com.project.orderservice.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public Order placeOrder(Order order) {
        // Step 1: Inventory check
        String checkUrl = inventoryServiceUrl + "/api/inventory/check?productId="
                + order.getProductId() + "&quantity=" + order.getQuantity();
        Boolean stockAvailable = restTemplate.getForObject(checkUrl, Boolean.class);

        if (!stockAvailable) {
            throw new RuntimeException("Stock not available");
        }

        // Step 2: Order save
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        // Step 3: Payment trigger
        String payUrl = paymentServiceUrl + "/api/payment-processor/pay?orderId="
                + saved.getId() + "&amount=" + saved.getTotalAmount();
        restTemplate.postForObject(payUrl, null, String.class);

        return saved;
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
    }
}