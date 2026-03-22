package com.project.orderservice.order.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderservice.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}

