<%--@elvariable id="dateNow" type="java.time.LocalDate"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Exchange rates</title>
</head>
<body>

    <h3>To obtain the average exchange rate and the standard deviation of the currency, <br>
        please pick the currency code and date range.</h3>

    <form action="rates" method="get">
        <label for="currencyCode">
            <select class="form-control" id="currencyCode" name="currencyCode">
                <%--@elvariable id="currencyTable" type="org.marcinski.exchangeRates.model.Table"--%>
                <c:forEach var="code" items="${currencyTable}">
                    <option name="${code}" value="${code}"> ${code} </option>
                </c:forEach>
            </select>
        </label><br>
        <label for="startDate">
            <input type="date" id="startDate" name="startDay" value="${dateNow}">
        </label><br>
        <label for="endDate">
            <input type="date" id="endDate" name="endDay" value="${dateNow}">
        </label><br>
        <button type="submit">Send</button>
    </form>

    <%--@elvariable id="average" type="java.lang.Double"--%>
    <c:if test="${average!=null}">
        <%--@elvariable id="currencyCode" type="java.lang.Double"--%>
        <%--@elvariable id="standardDeviance" type="java.lang.Double"--%>
        <h1>Average value for ${fn:toUpperCase(currencyCode)}: ${average}</h1>
        <h1>Standard deviance: ${standardDeviance}</h1>
    </c:if>

    <%--@elvariable id="errorMsg" type="java.lang.String"--%>
    <c:if test="${errorMsg != null}">
        <h1 style="color: red">${errorMsg}</h1>
    </c:if>

</body>
</html>
