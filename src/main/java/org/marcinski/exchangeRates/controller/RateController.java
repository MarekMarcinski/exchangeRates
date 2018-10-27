package org.marcinski.exchangeRates.controller;

import org.marcinski.exchangeRates.service.CurrencyRequestParameters;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rates")
public class RateController extends HttpServlet {

    private CurrencyRequestParameters parameters = new CurrencyRequestParameters();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        parameters.setLocalDate(req);
        parameters.setCurrencyCodesParameter(req);
        parameters.setParameters(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }
}