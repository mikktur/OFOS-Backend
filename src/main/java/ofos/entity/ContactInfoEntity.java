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



}
