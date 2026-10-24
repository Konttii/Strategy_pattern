package org.example.strategy;

import org.example.model.PaymentReceipt;

import java.util.Locale;

/**
 * Concrete Strategy 1: Processes instant payments via Kaspi QR token generation with 0% buyer fee
 * and automatic 5% Kaspi Bonus accrual.
 */
public class KaspiQRStrategy implements PaymentStrategy {
    private static final double BUYER_PROCESSING_FEE_KZT = 0.0;
    private static final double KASPI_BONUS_RATE = 0.05;

    private final String merchantTerminalId;
    private final String customerPhone;

    public KaspiQRStrategy(String merchantTerminalId, String customerPhone) {
        if (merchantTerminalId == null || merchantTerminalId.isBlank()) {
            throw new IllegalArgumentException("Kaspi merchant terminal ID must not be empty.");
        }
        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException("Customer phone number must not be empty.");
        }
        this.merchantTerminalId = merchantTerminalId;
        this.customerPhone = customerPhone;
    }

    @Override
    public PaymentReceipt processPayment(String orderId, double amountKzt) {
        validateAmount(amountKzt);
        double accruedBonusKzt = Math.round(amountKzt * KASPI_BONUS_RATE * 100.0) / 100.0;
        String transactionId = "KASPI-QR-" + Math.abs((orderId + merchantTerminalId).hashCode() % 900000 + 100000);
        String authSummary = String.format(
                Locale.US,
                "QR Scanned via Kaspi.kz (%s) | Terminal: %s | Accrued Bonus: +%.2f KZT (5%%)",
                customerPhone, merchantTerminalId, accruedBonusKzt
        );

        return new PaymentReceipt(
                transactionId,
                getGatewayName(),
                amountKzt,
                BUYER_PROCESSING_FEE_KZT,
                authSummary
        );
    }

    private void validateAmount(double amountKzt) {
        if (amountKzt <= 0) {
            throw new IllegalArgumentException("Payment amount must be strictly positive.");
        }
    }

    @Override
    public String getGatewayName() {
        return "Kaspi QR Instant Payment Gateway";
    }
}
