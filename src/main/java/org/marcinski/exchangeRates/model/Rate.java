package org.marcinski.exchangeRates.model;

import lombok.Getter;

public class Rate {
    private String no;
    private String effectiveDate;

    @Getter
    private double bid;

    @Getter
    private double ask;
}
