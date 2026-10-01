package com.example.store;

public class Restock {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.stockCount += 10;
        System.out.println("Stock after restock: "
                + inventory.stockCount);
    }
}
