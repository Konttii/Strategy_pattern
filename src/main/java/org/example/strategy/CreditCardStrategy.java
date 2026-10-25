package org.example.strategy;

import org.example.model.PaymentReceipt;

import java.util.Locale;

/**
 * Concrete Strategy 2: Processes bank card payments (Visa / Mastercard) with 3D-Secure verification
 * and a 2.5% + 150 KZT acquiring gateway fee.
 */
public class CreditCardStrategy implements PaymentStrategy {
    private static final double ACQUIRING_FEE_RATE = 0.025;
    private static final double FIXED_GATEWAY_FEE_KZT = 150.0;

    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate;

    public CreditCardStrategy(String cardNumber, String cardHolderName, String expiryDate) {
        if (cardNumber == null || cardNumber.replaceAll("\\s+", "").length() < 16) {
            throw new IllegalArgumentException("Card number must contain at least 16 digits.");
        }
        if (cardHolderName == null || cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Cardholder name must not be empty.");
        }
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must follow MM/YY format.");
        }
        this.cardNumber = cardNumber.replaceAll("\\s+", "");
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    @Override
    public PaymentReceipt processPayment(String orderId, double amountKzt) {
        if (amountKzt <= 0) {
            throw new IllegalArgumentException("Payment amount must be strictly positive.");
        }
        double acquiringFee = Math.round((amountKzt * ACQUIRING_FEE_RATE + FIXED_GATEWAY_FEE_KZT) * 100.0) / 100.0;
        String maskedCard = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        String transactionId = "CC-3DS-" + Math.abs((orderId + maskedCard).hashCode() % 900000 + 100000);
        String authSummary = String.format(
                Locale.US,
                "3D-Secure Verified | Card: %s (%s, exp %s) | Acquiring Fee: 2.5%% + 150 KZT",
                maskedCard, cardHolderName, expiryDate
        );

        return new PaymentReceipt(
                transactionId,
                getGatewayName(),
                amountKzt,
                acquiringFee,
                authSummary
        );
    }

    @Override
    public String getGatewayName() {
        return "Visa/Mastercard 3D-Secure Acquiring Gateway";
    }
}
