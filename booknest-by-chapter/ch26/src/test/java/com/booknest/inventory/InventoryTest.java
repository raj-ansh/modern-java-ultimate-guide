package com.booknest.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.booknest.catalog.Accessory;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {

    private final Product lamp =
            new Accessory("Reading lamp", "24.00");
    private Inventory inventory;

    @BeforeEach                       // runs before every test
    void stockTwoLamps() {
        inventory = new Inventory(new Catalog(lamp), 2);
    }

    @Test
    void reservingTakesItemsOutOfStock() throws OutOfStockException {
        inventory.reserve(lamp, 1);
        assertEquals(1, inventory.available(lamp));
    }

    @Test
    void reservingTooManyFailsAndLeavesStockUnchanged() {
        OutOfStockException e = assertThrows(
                OutOfStockException.class,
                () -> inventory.reserve(lamp, 3));
        assertEquals("only 2 x Reading lamp left (you asked for 3)",
                e.getMessage());
        assertEquals(2, inventory.available(lamp));
    }

    @Test
    void theLastItemCanBeSoldOnlyOnce() throws OutOfStockException {
        inventory.reserve(lamp, 2);
        OutOfStockException e = assertThrows(
                OutOfStockException.class,
                () -> inventory.reserve(lamp, 1));
        assertEquals("Reading lamp is sold out", e.getMessage());
    }

    @Test
    void quantitiesMustBePositive() {
        assertThrows(IllegalArgumentException.class,
                () -> inventory.reserve(lamp, 0));
    }
}
