package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "DeliveryAddresses", schema = "mikt")
public class DeliveryAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DeliveryAddressID", nullable = false)
    private int deliveryAddressId;
    @Basic
    @Column(name = "StreetAddress", nullable = false, length = 255)
    private String streetAddress;
    @Basic
    @Column(name = "City", nullable = false, length = 255)
    private String city;
    @Basic
    @Column(name = "PostalCode", nullable = false, length = 10)
    private String postalCode;
    public DeliveryAddressEntity(){}

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DeliveryAddressEntity that = (DeliveryAddressEntity) object;
        return deliveryAddressId == that.deliveryAddressId && Objects.equals(streetAddress, that.streetAddress) && Objects.equals(city, that.city) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetAddress, city, postalCode, deliveryAddressId);
    }
}
