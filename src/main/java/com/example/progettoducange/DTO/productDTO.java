package com.example.progettoducange.DTO;

import java.time.LocalDate;

public class productDTO {
    private String id;
    private String name;
    private int quantity;

    // #########################################################################################################
    //  LA DATA DI SCADENZA VA MESSA?
    // #########################################################################################################

    //private LocalDate date;

    public productDTO(String id, String name, Integer quantity)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        //this.date = date;
    }
    /*public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }*/

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
