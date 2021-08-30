<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Welcome to Gift Certificates System</h1>
<a href="<c:url value="/certificates"/>">Certificates</a> |
<a href="<c:url value="/certificates/create"/>">Create new certificate</a>
</body>
</html>