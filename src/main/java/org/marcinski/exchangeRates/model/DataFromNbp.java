package org.marcinski.exchangeRates.model;

import lombok.Data;

import java.util.List;

@Data
public class DataFromNbp {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
