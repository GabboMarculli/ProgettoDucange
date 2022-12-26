package com.example.progettoducange.DTO;

import java.time.LocalDate;

public class productDTO {

    private String name;
    private int quantity;
    private LocalDate expireDate;

    public productDTO(String name, int quantity, LocalDate expireDate) {
        this.name = name;
        this.quantity = quantity;
        this.expireDate = expireDate;
    }

    public LocalDate getDate() {
        return expireDate;
    }

    public void setDate(LocalDate date) {
        this.expireDate = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
