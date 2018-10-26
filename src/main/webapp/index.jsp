<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <h1>rate</h1>
    <%--@elvariable id="rates" type="org.marcinski.exchangeRates.model.Rate"--%>
    <c:forEach var="rate" items="${rates}">
        ${rate} <br>
    </c:forEach>
</body>
</html>
