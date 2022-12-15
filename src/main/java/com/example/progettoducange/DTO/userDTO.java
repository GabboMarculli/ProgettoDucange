package com.example.progettoducange.DTO;
import java.time.LocalDate;

public class userDTO {
    private long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private LocalDate registrationDate;
    private String country;

    public void setId(long id) {
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public void setUsername(String user){
        this.username = user;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}

