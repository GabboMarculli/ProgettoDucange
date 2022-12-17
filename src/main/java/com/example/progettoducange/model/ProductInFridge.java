package com.example.progettoducange.model;

import java.time.LocalDate;

public class ProductInFridge {
    private String name;
    private Integer quantity;
    private LocalDate expireDate;

    public ProductInFridge(String name, Integer quantity, LocalDate expireDate)
    {
        this.name = name;
        this.quantity = quantity;
        this.expireDate = expireDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }
}
