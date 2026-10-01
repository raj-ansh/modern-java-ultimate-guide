package com.booknest.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class CsvTest {

    @Test
    void splitsPlainFields() {
        assertEquals(List.of("book", "Dune", "9.99"),
                Csv.parseLine("book,Dune,9.99"));
    }

    @Test
    void keepsEmptyFields() {
        assertEquals(List.of("accessory", "Lamp", "", "24.00", ""),
                Csv.parseLine("accessory,Lamp,,24.00,"));
    }

    @Test
    void commasInsideQuotesAreData() {
        assertEquals(List.of("Tea, Toast and Tales", "9.75"),
                Csv.parseLine("\"Tea, Toast and Tales\",9.75"));
    }

    @Test
    void doubledQuotesInsideQuotesAreOneQuote() {
        assertEquals(List.of("The \"Best\" Book"),
                Csv.parseLine("\"The \"\"Best\"\" Book\""));
    }

    @Test
    void anUnclosedQuoteIsRejected() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Csv.parseLine("\"Tea, Toast"));
        assertEquals("a quote isn't closed", e.getMessage());
    }

    @Test
    void formattedLinesParseBackToTheSameFields() {
        List<String> fields =
                List.of("Tea, Toast", "The \"Best\"", "", "plain");
        String line = Csv.formatLine(fields.toArray(String[]::new));
        assertEquals(fields, Csv.parseLine(line));
    }
}
