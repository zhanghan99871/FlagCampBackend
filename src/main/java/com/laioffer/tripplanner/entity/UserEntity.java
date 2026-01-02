package com.laioffer.tripplanner.entity;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table(name = "users")
public class UserEntity {

    @Id
    private Long id;

    @Column("display_name")
    private String username;

    @Column("password_hash")
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

    public void setId(Long id) {this.id = id;}

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public Long getId() {  return id;}
    // getters & setters
}
