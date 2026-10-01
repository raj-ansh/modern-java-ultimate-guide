package com.example.store;

public class Inventory {

    int stockCount = 3;

    public boolean inStock() {
        return stockCount > 0;
    }
}
