<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Certificates</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
</head>
<body>
<div class="container mt-5">
    <c:forEach items="${certificateList}" var="certificate">
        <div class="row">
            <div class="col">
                    ${certificate.name}
            </div>
            <div class="col">
                    ${certificate.description}
            </div>
            <div class="col">
                    ${certificate.price}
            </div>
            <div class="col">
                    ${certificate.duration}
            </div>
            <div class="col">
                    ${certificate.createDate}
            </div>
            <div class="col">
                    ${certificate.lastUpdateDate}
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
