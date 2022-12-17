package com.example.progettoducange.model;


import java.time.LocalDate;
import java.util.List;

public class Fridge {
    private List<ProductInFridge> MyFridge;

    // constructor
    public Fridge(List<ProductInFridge> products) {
        this.MyFridge = products;
    }

    public void setMyFridge(List<ProductInFridge> myFridge) {
        MyFridge = myFridge;
    }

    public List<ProductInFridge> getMyFridge() {
        return MyFridge;
    }

    public void addProductToFridge(String product, LocalDate expireDate)
    {
        // search for product in fridge
       for(Integer i= 0; i< this.MyFridge.size();i++){
            if(MyFridge.get(i).getName().equals(product)) // if product is already in fridge
            {
                MyFridge.get(i).setQuantity(MyFridge.get(i).getQuantity()+1); // increment its quantity
            }
       }
       // else, add new row to fridge
        ProductInFridge newProduct = new ProductInFridge(product, 1 , expireDate);
        MyFridge.add(newProduct);
    }

    public void removeProduct(ProductInFridge product){
        MyFridge.remove(product);
    }

    public int getTotalNumberOfProduct() {
        return MyFridge.size();
    }

    public boolean isEmpty() {
        return MyFridge.isEmpty();
    }
}
