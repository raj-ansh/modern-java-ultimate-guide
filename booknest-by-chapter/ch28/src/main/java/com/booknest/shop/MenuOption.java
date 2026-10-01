package com.booknest.shop;

import java.util.Arrays;
import java.util.Optional;

/** The menu's commands, in the order they're shown. */
enum MenuOption {

    FIND_ORDER("7", "Find an order"),
    SEARCH("8", "Search"),
    VIEW_CART("9", "View cart"),
    CHECKOUT("0", "Checkout"),
    CLOSE("q", "Checkout and close the shop");

    private final String key;
    private final String label;

    MenuOption(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /** The line shown in the menu, such as "8) Search". */
    String menuLine() {
        return key + ") " + label;
    }

    /** Whether this choice ends the customer's visit. */
    boolean endsVisit() {
        return this == CHECKOUT || this == CLOSE;
    }

    /** The option for what the customer typed, if it is one. */
    static Optional<MenuOption> forKey(String key) {
        return Arrays.stream(values())
                .filter(option -> option.key.equals(key))
                .findFirst();
    }
}
