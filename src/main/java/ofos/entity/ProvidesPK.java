package ofos.entity;

import java.io.Serializable;
import java.util.Objects;


// Siirrä fiksumpaan paikkaan
// Luo pääavaimen kahdesta sarakkeesta.
public class ProvidesPK implements Serializable {

    private Integer RestaurantID;
    private Integer ProductID;

    public Integer getProductID() {
        return ProductID;
    }

    public void setProductID(Integer productID) {
        ProductID = productID;
    }



    public Integer getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        RestaurantID = restaurantID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProvidesPK that = (ProvidesPK) object;
        return RestaurantID == that.RestaurantID && ProductID == that.ProductID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RestaurantID, ProductID);
    }



}
