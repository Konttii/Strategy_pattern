package org.example.strategy;

import org.example.model.PaymentReceipt;

import java.util.Locale;

/**
 * Concrete Strategy 4: Demonstrates Open/Closed Principle extensibility by adding
 * Apple Pay NFC biometric token checkout without modifying CheckoutSession or existing strategies.
 */
public class ApplePayStrategy implements PaymentStrategy {
    private static final double TOKENIZATION_FEE_RATE = 0.008;

    private final String appleDeviceId;
    private final String biometricType;

    public ApplePayStrategy(String appleDeviceId, String biometricType) {
        if (appleDeviceId == null || appleDeviceId.isBlank()) {
            throw new IllegalArgumentException("Apple Device ID must not be empty.");
        }
        if (biometricType == null || biometricType.isBlank()) {
            throw new IllegalArgumentException("Biometric type must not be empty.");
        }
        this.appleDeviceId = appleDeviceId;
        this.biometricType = biometricType;
    }

    @Override
    public PaymentReceipt processPayment(String orderId, double amountKzt) {
        if (amountKzt <= 0) {
            throw new IllegalArgumentException("Payment amount must be strictly positive.");
        }
        double serviceFeeKzt = Math.round(amountKzt * TOKENIZATION_FEE_RATE * 100.0) / 100.0;
        String transactionId = "APAY-NFC-" + Math.abs((orderId + appleDeviceId).hashCode() % 900000 + 100000);
        String authSummary = String.format(
                Locale.US,
                "Secure Enclave Tokenized (%s) | Device: %s | Fee: 0.8%%",
                biometricType, appleDeviceId
        );

        return new PaymentReceipt(
                transactionId,
                getGatewayName(),
                amountKzt,
                serviceFeeKzt,
                authSummary
        );
    }

    @Override
    public String getGatewayName() {
        return "Apple Pay Biometric NFC Gateway";
    }
}
