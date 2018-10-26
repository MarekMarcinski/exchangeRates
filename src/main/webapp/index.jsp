<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <h3>To obtain the average exchange rate and the standard deviation of the currency, <br>
        please enter the currency code and date range.</h3>
    <h2 style="color: red">The date must be in a format: yyyy-mm-dd</h2>

    <form action="rates" method="get">
        <label for="currencyCode">
            <input type="text" id="currencyCode" name="currencyCode" placeholder="Currency code">
        </label><br>
        <label for="startDate">
            <input type="text" id="startDate" name="startDay" placeholder="Start date">
        </label><br>
        <label for="endDate">
            <input type="text" id="endDate" name="endDay" placeholder="End date">
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

</body>
</html>
