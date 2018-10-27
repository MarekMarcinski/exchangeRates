package org.marcinski.exchangeRates.service;

import com.google.gson.Gson;
import lombok.Setter;
import org.marcinski.exchangeRates.model.DataFromNbp;
import org.marcinski.exchangeRates.model.Rate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

class CurrencyValues {

    @Setter
    private String url;

    Double getAverageValueOfBids() throws IOException {
        List<Rate> rates = getRatesList();
        OptionalDouble optionalAverage = rates.stream()
                .map(Rate::getBid)
                .mapToDouble((x) -> x)
                .average();
        return getAverageValue(optionalAverage);
    }

    Double getStandardVariance() throws IOException {
        List<Rate> rates = getRatesList();
        List<Double> asks = rates.stream().map(Rate::getAsk).collect(Collectors.toList());

        double average = getAverageValueOfAsks();
        double temp = 0;
        for(double ask : asks)
            temp += (ask - average)*(ask - average);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.valueOf(df.format(Math.sqrt(temp/(asks.size()))));
    }

    private Double getAverageValueOfAsks() throws IOException {
        List<Rate> rates = getRatesList();
        OptionalDouble optionalAverage = rates.stream()
                .map(Rate::getAsk)
                .mapToDouble((x) -> x)
                .average();
        return getAverageValue(optionalAverage);
    }

    private Double getAverageValue(OptionalDouble optionalAverage) {
        Double average = null;
        if (optionalAverage.isPresent()){
            DecimalFormat df = new DecimalFormat("#.####");
            average = Double.valueOf(df.format(optionalAverage.getAsDouble()));
        }
        return average;
    }

    private List<Rate> getRatesList() throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.addRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        DataFromNbp dataFromNbp = getDataFromNbp(reader);

        return dataFromNbp.getRates();
    }

    private DataFromNbp getDataFromNbp(BufferedReader reader) throws IOException {
        Gson gson = new Gson();
        String data = reader.readLine();
        return gson.fromJson(data, DataFromNbp.class);
    }
}
