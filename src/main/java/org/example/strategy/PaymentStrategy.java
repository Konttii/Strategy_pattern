package org.example.strategy;

import org.example.model.PaymentReceipt;

/**
 * Strategy Interface declaring the common contract for all payment gateway algorithms.
 */
public interface PaymentStrategy {

    /**
     * Executes the payment algorithm for the specified order and amount.
     *
     * @param orderId   unique identifier of the customer order
     * @param amountKzt base order total in Kazakhstani Tenge (KZT)
     * @return structured PaymentReceipt detailing the transaction and gateway fees
     */
    PaymentReceipt processPayment(String orderId, double amountKzt);

    /**
     * Returns the display name of the payment gateway strategy.
     *
     * @return human-readable gateway description
     */
    String getGatewayName();
}
