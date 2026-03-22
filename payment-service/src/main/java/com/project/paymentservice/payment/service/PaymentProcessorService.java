package com.project.paymentservice.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.paymentservice.payment.entity.PaymentStatus;
import com.project.paymentservice.payment.entity.PaymentTransaction;
import com.project.paymentservice.payment.repo.PaymentTransactionRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentProcessorService {

    @Autowired
    private PaymentTransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    private static final int MAX_RETRY = 3;

    public PaymentTransaction initiatePayment(Long orderId, Double amount) {
        PaymentTransaction txn = PaymentTransaction.builder()
                .orderId(orderId)
                .amount(amount)
                .status(PaymentStatus.INITIATED)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        txn = transactionRepository.save(txn);
        return processPayment(txn);
    }

    private PaymentTransaction processPayment(PaymentTransaction txn) {
        txn.setStatus(PaymentStatus.PROCESSING);
        txn.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(txn);

        boolean fraudDetected = new Random().nextInt(10) < 2;
        boolean paymentSuccess = new Random().nextBoolean();

        if (fraudDetected) {
            txn.setStatus(PaymentStatus.FAILED_FINAL);
            txn.setFailureReason("Fraud detected");
            updateOrderStatus(txn.getOrderId(), "FAILED");
            reduceInventory(txn.getOrderId());
        } else if (paymentSuccess) {
            txn.setStatus(PaymentStatus.SUCCESS);
            updateOrderStatus(txn.getOrderId(), "CONFIRMED");
        } else {
            return retryPayment(txn);
        }

        txn.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(txn);
    }


    private void reduceInventory(Long orderId) {
    String orderUrl = orderServiceUrl + "/api/orders/" + orderId;
    // Order se productId aur quantity lo
    java.util.Map order = restTemplate.getForObject(orderUrl, java.util.Map.class);
    
    Long productId = Long.valueOf(order.get("productId").toString());
    Integer quantity = Integer.valueOf(order.get("quantity").toString());

    String url = inventoryServiceUrl + "/api/inventory/reduce?productId=" 
               + productId + "&quantity=" + quantity;
    restTemplate.put(url, null);
}

    private PaymentTransaction retryPayment(PaymentTransaction txn) {
        int retry = txn.getRetryCount() + 1;

        if (retry >= MAX_RETRY) {
            txn.setStatus(PaymentStatus.FAILED_FINAL);
            txn.setFailureReason("Max retries reached");
            updateOrderStatus(txn.getOrderId(), "FAILED");
        } else {
            txn.setStatus(PaymentStatus.RETRY);
            txn.setRetryCount(retry);
            txn.setUpdatedAt(LocalDateTime.now());
            transactionRepository.save(txn);
            return processPayment(txn);
        }

        txn.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(txn);
    }

    private void updateOrderStatus(Long orderId, String status) {
        String url = orderServiceUrl + "/api/orders/" + orderId + "/status?status=" + status;
        restTemplate.put(url, null);
    }

    public PaymentTransaction getTransactionByOrderId(Long orderId) {
        return transactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}