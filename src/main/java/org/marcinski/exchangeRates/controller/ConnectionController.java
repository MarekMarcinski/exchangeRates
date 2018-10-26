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
import java.util.List;

@WebServlet("/rates")
public class ConnectionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        URL obj = new URL("http://api.nbp.pl/api/exchangerates/rates/a/eur/2017-11-20/2017-11-24/");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.addRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        Gson gson = new Gson();
        String data = reader.readLine();
        DataFromNbp dataFromNbp = gson.fromJson(data, DataFromNbp.class);

        List<Rate> rates = dataFromNbp.getRates();
        req.setAttribute("rates", rates);

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }
}
