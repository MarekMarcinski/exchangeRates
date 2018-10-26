package org.marcinski.exchangeRates.controller;

import org.marcinski.exchangeRates.model.Table;
import org.marcinski.exchangeRates.service.CurrencyValues;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rates")
public class RateController extends HttpServlet {

    private CurrencyValues currencyValues = new CurrencyValues();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (nonEmptyParameters(req)) {
            setRequestParameters(req);
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    private boolean nonEmptyParameters(HttpServletRequest req) {
        return req.getParameter("currencyCode")!= null && !req.getParameter("currencyCode").isEmpty()
                && req.getParameter("startDay")!= null && !req.getParameter("startDay").isEmpty()
                && req.getParameter("endDay")!= null && !req.getParameter("endDay").isEmpty();
    }

    private void setRequestParameters(HttpServletRequest req) throws IOException {
        String currencyCode = req.getParameter("currencyCode").trim();
        String startDate = req.getParameter("startDay");
        String endDate = req.getParameter("endDay");
        String errorMsg;

        if (isDataInCorrectFormat(startDate, endDate) &&
                isCurrencyCodeCorrect(currencyCode) &&
                isEndDateAfterStartDate(startDate, endDate)) {

            String tableCode = getCodeOfRatesTable(currencyCode);

            boolean isOk = true;
            if (tableCode.equals("b")){
                isOk = isWednesdayForStartAndEnd(startDate, endDate);
            }

            if (isOk) {
                String url = String.format("http://api.nbp.pl/api/exchangerates/rates/%s/%s/%s/%s/",
                        tableCode, currencyCode, startDate, endDate);

                currencyValues.setUrl(url);

                Double average = currencyValues.getAverageValueOfRates();
                Double standardDeviance = currencyValues.getStandardVariance();

                req.setAttribute("average", average);
                req.setAttribute("standardDeviance", standardDeviance);
                req.setAttribute("currencyCode", currencyCode);

            }else {
                errorMsg = "For table B you must provide a wednesday";
                req.setAttribute("errorMsg", errorMsg);
            }
        }else if (!isDataInCorrectFormat(startDate, endDate)){
            errorMsg = "Date in wrong format!";
            req.setAttribute("errorMsg", errorMsg);
        }else if (!isEndDateAfterStartDate(startDate, endDate)){
            errorMsg = "End date must be after start date!";
            req.setAttribute("errorMsg", errorMsg);
        }else {
            errorMsg = "Wrong code of currency!";
            req.setAttribute("errorMsg", errorMsg);
        }
    }

    private boolean isDataInCorrectFormat(String startDate, String endDate) {
        return startDate.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))") &&
                endDate.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    }

    private boolean isCurrencyCodeCorrect(String currencyCode) {
        return Table.getTableA().contains(currencyCode.toUpperCase()) ||
                Table.getTableB().contains(currencyCode.toUpperCase());
    }

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return start.equals(end) || start.isBefore(end);
    }

    private String getCodeOfRatesTable(String currencyCode) {
        String tableCode;
        if (Table.getTableA().contains(currencyCode.toUpperCase())){
            tableCode = "a";
        }else {
            tableCode = "b";
        }
        return tableCode;
    }

    private boolean isWednesdayForStartAndEnd(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return start.getDayOfWeek().equals(end.getDayOfWeek());
    }
}
