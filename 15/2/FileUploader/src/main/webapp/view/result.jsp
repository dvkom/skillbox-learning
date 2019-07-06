<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Результаты загрузки</title>
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <link rel="stylesheet" href="style.css">
</head>
<body>
<header>
  <div class="w3-bar w3-border w3-blue-grey">
    <a href="index.html" class="w3-bar-item w3-button w3-hover-blue"><-Назад к загрузке файлов</a>
  </div>
</header>
<div class="w3-container w3-blue-grey w3-teal">
  <h2>Файл успешно загружен: <c:out value='${requestScope["fileName"]}'/></h2>
</div>
</body>
</html>
