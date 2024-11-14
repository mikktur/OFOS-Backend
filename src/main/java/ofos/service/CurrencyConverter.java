package ofos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CurrencyConverter {

    private static final Map<String, BigDecimal> exchangeRates = Map.of(
            "en", new BigDecimal("1.18"),
            "ja", new BigDecimal("130.0"),
            "ru", new BigDecimal("90.0")
    );

    public static BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        if (fromCurrency.equals("EUR")) {
            // Convert from euros to the target currency
            BigDecimal rate = exchangeRates.get(toCurrency);
            if (rate == null) {
                throw new IllegalArgumentException("Conversion rate not available for " + toCurrency);
            }
            return amount.multiply(rate);
        } else if (toCurrency.equals("EUR")) {
            // Convert from the source currency to euros
            BigDecimal rate = exchangeRates.get(fromCurrency);
            if (rate == null) {
                throw new IllegalArgumentException("Conversion rate not available for " + fromCurrency);
            }
            return amount.divide(rate, 2, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Only conversions to or from EUR are supported.");
        }
    }
}


