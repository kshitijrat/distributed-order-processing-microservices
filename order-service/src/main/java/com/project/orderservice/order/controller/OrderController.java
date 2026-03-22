package com.project.orderservice.order.controller;



import org.springframework.beans.factory.annotation.Autowired;
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
        return orderService.createOrder(order);
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Get Orders by User
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    // Update Order Status (for payment module)
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id,
                              @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }
}
