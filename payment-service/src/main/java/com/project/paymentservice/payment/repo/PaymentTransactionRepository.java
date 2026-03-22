package com.project.paymentservice.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.paymentservice.payment.entity.PaymentTransaction;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByOrderId(Long orderId);
}
