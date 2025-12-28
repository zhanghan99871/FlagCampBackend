package com.laioffer.tripplanner.entity;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
@Table(name = "users")
public class UserEntity {

    @Id
    private Long id;

    private String username;

    private String password;

//    email is true id
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    // getters & setters
}
