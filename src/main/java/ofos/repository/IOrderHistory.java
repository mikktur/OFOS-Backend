package ofos.repository;

import java.math.BigDecimal;
import java.sql.Date;

// Tarvitsee interfacen, koska historian haussa yhdistetään sarakkeita useasta pöydästä niin ei voi käyttää entityä.

public interface IOrderHistory {

    int getOrderID();

    BigDecimal getProductPrice();

    int getQuantity();

    String getProductName();

    Date getOrderDate();

    int getRestaurantID();

    String getProductDesc();

    int getProductID();

}
