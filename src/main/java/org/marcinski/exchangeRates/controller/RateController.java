package org.marcinski.exchangeRates.controller;

import com.google.gson.Gson;
import org.marcinski.exchangeRates.model.DataFromNbp;
import org.marcinski.exchangeRates.model.Rate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@WebServlet("/rates")
public class RateController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (nonEmptyParameters(req)) {
            String currencyCode = req.getParameter("currencyCode");
            String startDate = req.getParameter("startDay");
            String endDate = req.getParameter("endDay");

            String url = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s/%s/%s/",
                    currencyCode, startDate, endDate);

            List<Rate> rates = getRatesList(url);

            Double average = getAverageValueOfRates(rates);
            Double standatdDeviance = getStandardVariance(rates);

            if (average!=null) {
                req.setAttribute("average", average);
                req.setAttribute("standardDeviance", standatdDeviance);
                req.setAttribute("currencyCode", currencyCode);
            }
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    private boolean nonEmptyParameters(HttpServletRequest req) {
        return req.getParameter("currencyCode")!= null && !req.getParameter("currencyCode").isEmpty()
            && req.getParameter("startDay")!= null && !req.getParameter("startDay").isEmpty()
            && req.getParameter("endDay")!= null && !req.getParameter("endDay").isEmpty();
    }

    private List<Rate> getRatesList(String url) throws IOException {
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

    private Double getAverageValueOfRates(List<Rate> rates) {
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

    private Double getStandardVariance(List<Rate> rates) {
        List<Double> mids = rates.stream().map(Rate::getMid).collect(Collectors.toList());

        double average = getAverageValueOfRates(rates);
        double temp = 0;
        for(double mid : mids)
            temp += (mid - average)*(mid - average);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.valueOf(df.format(Math.sqrt(temp/(mids.size()))));
    }
}
