package ofos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CurrencyConverter {

    private static final Map<String, BigDecimal> exchangeRates = Map.of(
            "fi", BigDecimal.ONE,
            "en", new BigDecimal("1.18"),
            "ja", new BigDecimal("130.0"),
            "ru", new BigDecimal("90.0")
    );

    public static BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative and not null");
        }
        if(toCurrency.equals(fromCurrency)){
            return amount;
        }
        if (fromCurrency.equals("fi")) {
            // Convert from euros to the target currency
            BigDecimal rate = exchangeRates.get(toCurrency);
            if (rate == null) {
                throw new IllegalArgumentException("Conversion rate not available for " + toCurrency);
            }
            return amount.multiply(rate);
        } else if (toCurrency.equals("fi")) {
            // Convert from the source currency to euros
            BigDecimal rate = exchangeRates.get(fromCurrency);
            if (rate == null) {
                throw new IllegalArgumentException("Conversion rate not available for " + fromCurrency);
            }
            return amount.divide(rate, 2, RoundingMode.HALF_UP);
        } else {
            // Non-EUR to Non-EUR conversion
            BigDecimal rateFrom = exchangeRates.get(fromCurrency);
            BigDecimal rateTo = exchangeRates.get(toCurrency);
            if (rateFrom == null || rateTo == null) {
                throw new IllegalArgumentException("Conversion rate not available for " + fromCurrency + " or " + toCurrency);
            }
            return amount.multiply(rateTo).divide(rateFrom, 2, RoundingMode.HALF_UP);
        }
    }
}


