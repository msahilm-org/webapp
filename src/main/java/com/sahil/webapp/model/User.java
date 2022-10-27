package com.sahil.webapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name="user_id")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID id;
    @JsonProperty("first_name")
    @Column(name="first_name")
    private String firstName;

    @JsonProperty("last_name")
    @Column(name="last_name")
    private String lastName;
    @JsonProperty("password")
    @Column(name="password")
    private String password;
    @JsonProperty("username")
    @Column(name="username")
    private String username;

    //@JsonProperty("account_created_on")
    @JsonProperty("account_created")
    @Column(name="account_created_on")
    private Timestamp accountCreated;

    //@JsonProperty("account_updated_on")
    @JsonProperty("account_updated")
    @Column(name="account_updated_on")
    private Timestamp accountUpdated;


    public User(){}

    public User(String firstName, String lastName, String password, String username, Timestamp accountCreated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.accountCreated= accountCreated;
    }

    /*Getters*/
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public UUID getId() {
        return id;
    }

    public Timestamp getAccountCreated() {
        return accountCreated;
    }

    public Timestamp getAccountUpdated() {
        return accountUpdated;
    }
    /*Setters*/

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountCreated(Timestamp accountCreated) {
        this.accountCreated = accountCreated;
    }

    public void setAccountUpdated(Timestamp accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
