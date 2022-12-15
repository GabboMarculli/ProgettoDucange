package com.example.progettoducange.DTO;
import java.util.ArrayList;

public class fridgeDTO {
    private long id;
    private String name;
    private String owner;

    private  ArrayList<String> product;

    public void setProduct(ArrayList<String> products){
        this.product = products;
    }

    public ArrayList<String> getProduct(){
        return product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
