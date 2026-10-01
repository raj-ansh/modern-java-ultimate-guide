public class Literals {

    public static void main(String[] args) {
        long worldPopulation = 8_100_000_000L;
        int booksSold = 1_250_000;
        float discount = 0.15f;
        double averagePrice = 18.75;
        char grade = 'A';
        boolean isBestseller = booksSold > 1_000_000;

        System.out.println("World population: " + worldPopulation);
        System.out.println("Books sold:       " + booksSold);
        System.out.println("Discount:         " + discount);
        System.out.println("Average price:    " + averagePrice);
        System.out.println("Grade:            " + grade);
        System.out.println("Bestseller?       " + isBestseller);
    }
}
