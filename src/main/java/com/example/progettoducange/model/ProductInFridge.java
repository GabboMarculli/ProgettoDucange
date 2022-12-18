package com.example.progettoducange.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class ProductInFridge {
    final private SimpleStringProperty name;
    final private SimpleIntegerProperty quantity;
    final private SimpleLocalDateProperty expireDate;

    public ProductInFridge(String name, Integer quantity, LocalDate expireDate)
    {

        this.quantity = new SimpleIntegerProperty(quantity);
        this.expireDate = new SimpleLocalDateProperty(expireDate);
        this.name = new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LocalDate getExpireDate() {
        return expireDate.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate.set(expireDate);
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    public String getName() {
        return name.get();
    }
}
