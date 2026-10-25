package org.example;

import org.example.context.CheckoutSession;
import org.example.strategy.ApplePayStrategy;
import org.example.strategy.CreditCardStrategy;
import org.example.strategy.CryptoWalletStrategy;
import org.example.strategy.KaspiQRStrategy;

/**
 * Client class demonstrating runtime selection and dynamic switching of PaymentStrategy
 * implementations inside CheckoutSession without any conditional logic in the Context.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Payment Processing Gateway Integration (Strategy Pattern) ===\n");

        // 1. Initialize CheckoutSession with KaspiQRStrategy
        CheckoutSession checkoutSession = new CheckoutSession(
                "ORD-KZ-2026-884",
                "Kuandyk Koshanov",
                145000.00,
                new KaspiQRStrategy("KASPI-TERM-ASTANA-09", "+7 (707) 555-82-19")
        );

        System.out.println("[Step 1] Initial Payment Strategy -> Kaspi QR Instant Checkout:");
        System.out.println(checkoutSession.executeCheckout());

        // 2. Dynamically switch strategy at runtime to CreditCardStrategy
        checkoutSession.setPaymentStrategy(
                new CreditCardStrategy("4400 4301 8821 9045", "KUANDYK KOSHANOV", "08/29")
        );
        System.out.println("\n[Step 2] Runtime Strategy Switch -> Credit Card (Visa/Mastercard 3D-Secure):");
        System.out.println(checkoutSession.executeCheckout());

        // 3. Dynamically switch strategy at runtime to CryptoWalletStrategy
        checkoutSession.setPaymentStrategy(
                new CryptoWalletStrategy("0x71C9B8A4E2F09183D4A56B72E891C3029485A91C", "TRC-20 / USDT")
        );
        System.out.println("\n[Step 3] Runtime Strategy Switch -> Crypto Wallet (USDT On-Chain):");
        System.out.println(checkoutSession.executeCheckout());

        // 4. Extensibility demonstration: switch to newly added ApplePayStrategy without modifying Context
        checkoutSession.setPaymentStrategy(
                new ApplePayStrategy("iPhone-16-Pro-Enclave-KK", "FaceID")
        );
        System.out.println("\n[Step 4] Extensibility Demo -> Newly Added ApplePayStrategy:");
        System.out.println(checkoutSession.executeCheckout());
    }
}
