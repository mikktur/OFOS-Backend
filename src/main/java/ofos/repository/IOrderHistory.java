package ofos.repository;

import java.math.BigDecimal;

// Tarvitsee interfacen, koska historian haussa yhdistetään sarakkeita useasta pöydästä niin ei voi käyttää entityä.

public interface IOrderHistory {

    int getOrderID();

    BigDecimal getProductPrice();

    int getQuantity();

    String getProductName();

}
