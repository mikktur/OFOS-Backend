package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ContactInfo", schema = "mikt")
public class ContactInfoEntity {
    @Id
    @Column(name = "User_ID")
    private int userId;
    @Column(name = "PhoneNumber")
    private String phoneNumber;
    @Basic
    @Column(name = "Address")
    private String address;

    public ContactInfoEntity(){}


    @Basic
    @Column(name = "City")
    private String city;
    @Basic
    @Column(name = "FirstName")
    private String firstName;
    @Basic
    @Column(name = "LastName")
    private String lastName;
    @Basic
    @Column(name = "Email")
    private String email;
    @Basic
    @Column(name = "PostalCode")
    private String postalCode;


    // Testiä varten
    public ContactInfoEntity(int userId, String phoneNumber, String address, String city, String firstName, String lastName, String email, String postalCode) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postalCode = postalCode;
    }

}
