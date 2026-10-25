package org.example.strategy;

import org.example.model.PaymentReceipt;

import java.util.Locale;

/**
 * Concrete Strategy 3: Processes decentralized cryptocurrency payments (USDT) on-chain,
 * applying a 1.0% blockchain network gas fee and converting KZT to USDT.
 */
public class CryptoWalletStrategy implements PaymentStrategy {
    private static final double NETWORK_GAS_FEE_RATE = 0.01;
    private static final double USDT_EXCHANGE_RATE_KZT = 485.0;

    private final String walletAddress;
    private final String blockchainNetwork;

    public CryptoWalletStrategy(String walletAddress, String blockchainNetwork) {
        if (walletAddress == null || walletAddress.length() < 12) {
            throw new IllegalArgumentException("Crypto wallet address is invalid or too short.");
        }
        if (blockchainNetwork == null || blockchainNetwork.isBlank()) {
            throw new IllegalArgumentException("Blockchain network must not be empty.");
        }
        this.walletAddress = walletAddress;
        this.blockchainNetwork = blockchainNetwork;
    }

    @Override
    public PaymentReceipt processPayment(String orderId, double amountKzt) {
        if (amountKzt <= 0) {
            throw new IllegalArgumentException("Payment amount must be strictly positive.");
        }
        double networkGasFeeKzt = Math.round(amountKzt * NETWORK_GAS_FEE_RATE * 100.0) / 100.0;
        double totalUsdtEquivalent = (amountKzt + networkGasFeeKzt) / USDT_EXCHANGE_RATE_KZT;
        String shortenedWallet = walletAddress.substring(0, 6) + "..." + walletAddress.substring(walletAddress.length() - 4);
        String txHash = "0x" + Integer.toHexString(Math.abs((orderId + walletAddress).hashCode())).toUpperCase(Locale.US) + "F9A2";
        String authSummary = String.format(
                Locale.US,
                "On-Chain Confirmed (%s) | Wallet: %s | Converted: %.2f USDT (@ %.0f KZT/USDT)",
                blockchainNetwork, shortenedWallet, totalUsdtEquivalent, USDT_EXCHANGE_RATE_KZT
        );

        return new PaymentReceipt(
                txHash,
                getGatewayName(),
                amountKzt,
                networkGasFeeKzt,
                authSummary
        );
    }

    @Override
    public String getGatewayName() {
        return "Web3 Crypto Wallet Gateway (" + blockchainNetwork + ")";
    }
}
