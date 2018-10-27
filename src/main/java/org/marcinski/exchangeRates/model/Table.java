package org.marcinski.exchangeRates.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Table {

    private static final List<String> tableC = Arrays.asList("USD", "AUD", "CAD",
            "EUR", "HUF", "CHF", "GBP", "JPY", "CZK", "DKK", "NOK", "SEK", "XDR");

    public static List<String> getTableC() {
        return Collections.unmodifiableList(tableC);
    }
}