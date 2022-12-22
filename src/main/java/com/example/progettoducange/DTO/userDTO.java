package com.example.progettoducange.DTO;
import com.example.progettoducange.model.SimpleLocalDateProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

import static java.time.LocalDate.now;

public class userDTO {
    private long id;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleLocalDateProperty registrationDate = new SimpleLocalDateProperty(now());
    private SimpleStringProperty country = new SimpleStringProperty();

    public userDTO(long id,String u,String p, String n,String sn,
                   LocalDate r, String c) {
        username.set(u);
        password.set(p);
        name.set(n);
        surname.set(sn);
        registrationDate.set(r);
        country.set(c);
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public void setUsername(String user){
        this.username.set(user);
    }

    public String getUsername(){
        return username.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPassword() {
        return password.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public String getName(){
        return name.get();
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname){
        this.surname.set(surname);
    }

    public LocalDate getRegistrationDate() {
        return registrationDate.get();
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCountry() {
        return country.get();
    }
}

