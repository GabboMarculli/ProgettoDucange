package com.example.progettoducange.model;

import java.time.LocalDate;
import java.util.Date;

public abstract class User {
    public int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String country;

    private LocalDate registrationDate;
    private String email;

    public void User(int id, String username, String password,LocalDate registrationDate )
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registrationDate=registrationDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public  String getUsername() {
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

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail()
    {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
