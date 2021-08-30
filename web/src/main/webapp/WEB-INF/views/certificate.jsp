<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>Certificate</title>
</head>
<body>
<div class="container mt-5">
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
</div>
</body>
</html>