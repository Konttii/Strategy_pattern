package org.example.context;

import org.example.model.PaymentReceipt;
import org.example.strategy.PaymentStrategy;

import java.util.Locale;

/**
 * Context class in the Strategy pattern (CheckoutSession).
 * Maintains a reference to a PaymentStrategy and delegates payment execution to it.
 * Completely independent of concrete gateway internals and contains zero if/switch branching on strategy types.
 */
public class CheckoutSession {
    private final String orderId;
    private final String customerName;
    private final double orderTotalKzt;
    private PaymentStrategy paymentStrategy;

    public CheckoutSession(String orderId, String customerName, double orderTotalKzt, PaymentStrategy initialStrategy) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID must not be empty.");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }
        if (orderTotalKzt <= 0) {
            throw new IllegalArgumentException("Order total must be strictly positive.");
        }
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderTotalKzt = orderTotalKzt;
        setPaymentStrategy(initialStrategy);
    }

    /**
     * Switches the active payment gateway strategy at runtime.
     *
     * @param paymentStrategy the new payment algorithm to use
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        if (paymentStrategy == null) {
            throw new IllegalArgumentException("PaymentStrategy must not be null.");
        }
        this.paymentStrategy = paymentStrategy;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    /**
     * Delegates payment processing to the currently configured PaymentStrategy
     * and formats the checkout confirmation report.
     *
     * @return formatted transaction summary
     */
    public String executeCheckout() {
        PaymentReceipt receipt = paymentStrategy.processPayment(orderId, orderTotalKzt);

        return String.format(
                Locale.US,
                """
                --------------------------------------------------------------------------------
                Order ID        : %s (Customer: %s)
                Active Gateway  : %s
                Transaction ID  : %s
                Order Subtotal  : %.2f KZT
                Gateway Fee     : +%.2f KZT
                Total Charged   : %.2f KZT
                Gateway Details : %s
                --------------------------------------------------------------------------------""",
                orderId,
                customerName,
                receipt.getGatewayName(),
                receipt.getTransactionId(),
                receipt.getBaseAmountKzt(),
                receipt.getProcessingFeeKzt(),
                receipt.getTotalChargedKzt(),
                receipt.getAuthorizationDetails()
        );
    }
}
