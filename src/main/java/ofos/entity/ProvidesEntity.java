package ofos.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Provides")
@IdClass(ProvidesPK.class)
public class ProvidesEntity {

    @Id @Column(name="RestaurantID")
    private Integer RestaurantID;
    @Id @Column(name="ProductID")
    private Integer ProductID;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProvidesEntity that = (ProvidesEntity) object;
        return RestaurantID == that.RestaurantID && ProductID == that.ProductID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RestaurantID, ProductID);
    }

    public ProvidesEntity(){
    }

    public void setProductID(Integer productID) {
        ProductID = productID;
    }

    public Integer getProductID() {
        return ProductID;
    }

    public void setRestaurantID(Integer restaurantID) {
        RestaurantID = restaurantID;
    }

    public Integer getRestaurantID() {
        return RestaurantID;
    }
}
