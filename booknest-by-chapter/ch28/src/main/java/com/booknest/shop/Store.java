package com.booknest.shop;

import java.time.ZoneId;

/** Facts about the physical shop that several classes share. */
final class Store {

    /** The shop's own time zone: receipts and reports use it. */
    static final ZoneId ZONE = ZoneId.of("America/Chicago");

    private Store() {
        // constants only
    }
}
