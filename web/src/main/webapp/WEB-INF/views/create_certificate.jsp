<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create certificate</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
</head>
<body>
<div class="container mt-5">
    <h1>Create new certificate</h1>
    <form method="post">
        <label for="nameInput" class="form-label">Name</label>
        <input id="nameInput" type="text" name="certificateName"/><br/>

        <label for="descriptionTextArea" class="form-label">Description</label>
        <textarea id="descriptionTextArea" name="certificateDescription"></textarea><br/>

        <label for="priceInput" class="form-label">Price</label>
        <input id="priceInput" type="number" name="certificatePrice"/><br/>

        <label for="durationInput" class="form-label">Duration</label>
        <input id="durationInput" type="number" name="certificateDuration"/><br/>

        <button type="submit">Create</button>
    </form>
</div>
</body>
</html>
