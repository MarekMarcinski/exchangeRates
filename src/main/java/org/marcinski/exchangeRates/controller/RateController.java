package org.marcinski.exchangeRates.controller;

import org.marcinski.exchangeRates.service.CurrencyValues;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rates")
public class RateController extends HttpServlet {

    private CurrencyValues currencyValues = new CurrencyValues();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (nonEmptyParameters(req)) {
            String currencyCode = req.getParameter("currencyCode");
            String startDate = req.getParameter("startDay");
            String endDate = req.getParameter("endDay");

            String url = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s/%s/%s/",
                    currencyCode, startDate, endDate);

            currencyValues.setUrl(url);

            Double average = currencyValues.getAverageValueOfRates();
            Double standatdDeviance = currencyValues.getStandardVariance();

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
}
