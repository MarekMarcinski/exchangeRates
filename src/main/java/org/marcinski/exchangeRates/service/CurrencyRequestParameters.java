package org.marcinski.exchangeRates.service;

import org.marcinski.exchangeRates.model.Table;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;

public class CurrencyRequestParameters {

    private CurrencyValues currencyValues = new CurrencyValues();

    public  void setLocalDate(HttpServletRequest req){
        req.setAttribute("dateNow", LocalDate.now());
    }

    public void setCurrencyCodesParameter(HttpServletRequest req){
        req.setAttribute("currencyTable", Table.getTableC());
    }

    public void setParameters(HttpServletRequest req) throws IOException {
        if (nonEmptyParameters(req)) {
            setRequestParameters(req);
        }
    }

    private void setRequestParameters(HttpServletRequest req) throws IOException {
        String currencyCode = req.getParameter("currencyCode").trim();
        String startDate = req.getParameter("startDay");
        String endDate = req.getParameter("endDay");
        String errorMsg;

        if (isDataInCorrectFormat(startDate, endDate) &&
            isCurrencyCodeCorrect(currencyCode) &&
            isEndDateAfterStartDate(startDate, endDate) &&
            !areDatesAfterToday(startDate, endDate)) {

            String url = String.format("http://api.nbp.pl/api/exchangerates/rates/c/%s/%s/%s/",
                    currencyCode, startDate, endDate);

            currencyValues.setUrl(url);

            Double average = currencyValues.getAverageValueOfBids();
            Double standardDeviance = currencyValues.getStandardVariance();

            req.setAttribute("average", average);
            req.setAttribute("standardDeviance", standardDeviance);
            req.setAttribute("currencyCode", currencyCode);

        }else if (!isDataInCorrectFormat(startDate, endDate)){
            errorMsg = "Date in wrong format! Acceptable format yyyy-mm-dd";
            req.setAttribute("errorMsg", errorMsg);
        }else if (!isEndDateAfterStartDate(startDate, endDate)){
            errorMsg = "End date must be after start date!";
            req.setAttribute("errorMsg", errorMsg);
        }else if (areDatesAfterToday(startDate, endDate)){
            errorMsg = "Dates can not be from the future!";
            req.setAttribute("errorMsg", errorMsg);
        }else {
            errorMsg = "Wrong code of currency!";
            req.setAttribute("errorMsg", errorMsg);
        }
    }

    private boolean nonEmptyParameters(HttpServletRequest req) {
        return req.getParameter("currencyCode")!= null && !req.getParameter("currencyCode").isEmpty()
                && req.getParameter("startDay")!= null && !req.getParameter("startDay").isEmpty()
                && req.getParameter("endDay")!= null && !req.getParameter("endDay").isEmpty();
    }

    private boolean isDataInCorrectFormat(String startDate, String endDate) {
        return startDate.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))") &&
                endDate.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    }

    private boolean isCurrencyCodeCorrect(String currencyCode) {
        return Table.getTableC().contains(currencyCode.toUpperCase());
    }

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return start.isBefore(end);
    }

    private boolean areDatesAfterToday(String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate now = LocalDate.now();
        return start.isAfter(now) || end.isAfter(now);
    }
}
