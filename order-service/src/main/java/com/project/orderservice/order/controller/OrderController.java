package com.project.orderservice.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.orderservice.order.entity.Order;
import com.project.orderservice.order.entity.OrderStatus;
import com.project.orderservice.order.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place Order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    // Get Orders by User
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    // Update Order Status (for payment module)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
            @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok("Status updated");
    }
}
