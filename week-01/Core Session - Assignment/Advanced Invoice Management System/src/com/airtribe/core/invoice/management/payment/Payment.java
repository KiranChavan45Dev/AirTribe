package com.airtribe.core.invoice.management.payment;

import com.airtribe.core.invoice.management.invoice.PaymentMethod;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private PaymentMethod method;
    private double amountPaid;
    private LocalDateTime date;

    public Payment() {
    }

    public Payment(String paymentId, PaymentMethod method, double amountPaid, LocalDateTime date) {
        this.paymentId = paymentId;
        this.method = method;
        this.amountPaid = amountPaid;
        this.date = date;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", method=" + method +
                ", amountPaid=" + amountPaid +
                ", date=" + date +
                '}';
    }
}
