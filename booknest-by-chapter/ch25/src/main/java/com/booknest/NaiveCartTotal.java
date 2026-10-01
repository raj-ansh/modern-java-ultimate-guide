package com.booknest;

public class NaiveCartTotal {

    public static void main(String[] args) {
        double novel = 12.99;
        double notebook = 7.49;
        double bookmark = 3.35;

        double total = novel + notebook + bookmark;
        System.out.println("Cart total: " + total);

        int cents = (int) (4.35 * 100);
        System.out.println("4.35 in cents: " + cents);
    }
}
