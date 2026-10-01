package com.booknest;

public class BookNestApp {

    public static void main(String[] args) {
        Banner.show();

        String customer = "Guest";
        if (args.length > 0) {
            customer = args[0];
        }

        System.out.println("Hello, " + customer + "! Happy reading.");
        int javaVersion = Runtime.version().feature();
        System.out.println("Running on Java " + javaVersion);
    }
}
