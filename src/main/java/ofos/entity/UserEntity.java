package ofos.entity;

import jakarta.persistence.*;

import java.util.Set;

/**
 * UserEntity class is an entity class that maps to the Users table in the database.
 * It contains the following columns:
 * - User_ID
 * - Username
 * - Password
 * - Role
 */

@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Integer userId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role = "USER";


    // Constructors
    public UserEntity() {
    }

    public UserEntity(Integer userId, String username, String password, String role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return userId;
    }

}
