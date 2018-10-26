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

public class CurrencyValues {

    @Setter
    private String url;

    public Double getAverageValueOfRates() throws IOException {
        List<Rate> rates = getRatesList();
        OptionalDouble optionalAverage = rates.stream()
                .map(Rate::getMid)
                .mapToDouble((x) -> x)
                .average();
        Double average = null;
        if (optionalAverage.isPresent()){
            DecimalFormat df = new DecimalFormat("#.####");
            average = Double.valueOf(df.format(optionalAverage.getAsDouble()));
        }
        return average;
    }

    public Double getStandardVariance() throws IOException {
        List<Rate> rates = getRatesList();
        List<Double> mids = rates.stream().map(Rate::getMid).collect(Collectors.toList());

        double average = getAverageValueOfRates();
        double temp = 0;
        for(double mid : mids)
            temp += (mid - average)*(mid - average);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.valueOf(df.format(Math.sqrt(temp/(mids.size()))));
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
