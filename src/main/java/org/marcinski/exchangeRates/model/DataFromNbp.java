package org.marcinski.exchangeRates.model;

import lombok.Getter;

import java.util.List;

public class DataFromNbp {

    private String table;
    private String currency;
    private String code;

    @Getter
    private List<Rate> rates;
}
