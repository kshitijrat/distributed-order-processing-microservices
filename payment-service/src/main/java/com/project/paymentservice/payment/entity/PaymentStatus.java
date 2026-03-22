package com.project.paymentservice.payment.entity;
public enum PaymentStatus {
    INITIATED,
    PROCESSING,
    SUCCESS,
    FAILED,
    RETRY,
    FAILED_FINAL
}
