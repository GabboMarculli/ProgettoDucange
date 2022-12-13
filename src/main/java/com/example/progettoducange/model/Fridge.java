package com.example.progettoducange.model;


import java.util.HashMap;

public class Fridge {
    // first Long is product id, second integer is quantity
    // Example: milk's id is 4, bread's id is 7, pizza's id is 16. If I put in my fridge 3 milk, 2 bread and 2 pizza,
    // the hashMap is ->  { (4,3) , (7,2) , (16,2) }
    public HashMap<Long, Integer> products = new HashMap<Long, Integer>();

    // constructor
    public Fridge(Long product, Integer quantity) {
        this.products.put(product, quantity);
    }

    public void setFridge(HashMap<Long, Integer> products) {
        this.products = products;
    }

    public void addProductToFridge(Long product)
    {
        // if product is yet in fridge, increment its value, else insert new product with value '1'
        products.put(product, products.containsKey(product) ? products.get(product) + 1 : 1);
    }

    public void removeProduct(Long productId){
        products.remove(productId);
    }

    public HashMap<Long, Integer> getProducts() {
        return products;
    }

    public int getTotalNumberOfProduct() {
        return products.size();
    }

    public boolean isEmpty(){
        return products.isEmpty();
    }

    @Override
    public String toString() {
        return "Fridge{" +
                "products=" + products +
                '}';
    }
}
