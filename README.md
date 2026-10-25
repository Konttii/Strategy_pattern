# Assignment #5 — Strategy Pattern (Payment Gateway Processing)

**Course:** ShP-2216 – Software Design Patterns  
**Institution:** Astana IT University  
**Student:** Kuandyk Koshanov  
**Topic:** Option C — Payment Processing Gateway Integration (`CheckoutSession`)  
**Java Version:** JDK 21 (Maven)

---

## Project Overview

This project implements the **Strategy** behavioral design pattern in Java 21. It models an e-commerce checkout process (`CheckoutSession`) that executes payments through interchangeable payment gateway algorithms (`PaymentStrategy`):
1. **`KaspiQRStrategy`** — processes instant QR payments with 0% buyer fee and 5% Kaspi Bonus accrual.
2. **`CreditCardStrategy`** — processes Visa/Mastercard transactions with 3D-Secure validation and acquiring fees (2.5% + 150 KZT).
3. **`CryptoWalletStrategy`** — processes on-chain USDT cryptocurrency payments with KZT-to-USDT conversion and 1.0% network gas fee.
4. **`ApplePayStrategy`** — demonstrates Open/Closed Principle extensibility by adding tokenized FaceID NFC checkout without modifying `CheckoutSession` or any existing strategy.

## Folder Structure

```text
src/main/java/org/example/
├── Main.java                                # Client: configures and switches payment strategies at runtime
├── context/
│   └── CheckoutSession.java                 # Context: holds PaymentStrategy reference and delegates execution
├── model/
│   └── PaymentReceipt.java                  # Domain Model: immutable transaction receipt
└── strategy/
    ├── PaymentStrategy.java                 # Strategy Interface: common payment contract
    ├── KaspiQRStrategy.java                 # Concrete Strategy 1: Kaspi QR instant payment
    ├── CreditCardStrategy.java              # Concrete Strategy 2: Visa/Mastercard 3D-Secure acquiring
    ├── CryptoWalletStrategy.java            # Concrete Strategy 3: Web3 Crypto Wallet (USDT)
    └── ApplePayStrategy.java                # Concrete Strategy 4: Apple Pay NFC (Open/Closed extensibility demo)
```

## How to Build & Run (Java 21)

```bash
javac --release 21 -d out $(find src/main/java -name "*.java")
java -cp out org.example.Main
```
