<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>WebVoteAnalyzer: результаты анализа</title>
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <link rel="stylesheet" href="style.css">
  <script>
    window.onscroll = function () {
      scrollFunction()
    };

    function scrollFunction() {
      if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("toTopButton").style.display = "block";
      } else {
        document.getElementById("toTopButton").style.display = "none";
      }
    }

    function topFunction() {
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
    }
  </script>
</head>
<body>
<header>
  <div class="w3-bar w3-border w3-blue-grey">
    <a href="index.html" class="w3-bar-item w3-button w3-hover-blue"><-Назад к загрузке файлов</a>
  </div>
</header>
<div class="w3-container w3-blue-grey w3-teal">
  <h1>Расписание работы участков:</h1>
</div>
<table class="w3-table w3-striped w3-border">
  <c:set var="voteStationsSchedule" scope="session" value='${requestScope["voteStationsSchedule"]}'/>
  <c:set var="workDays" scope="session" value='${voteStationsSchedule.workDays}'/>
  <c:set var="schedule" scope="session" value='${voteStationsSchedule.schedule}'/>

  <c:if test="${workDays != null}">
    <tr class=\"w3-blue\">
      <td>Участок</td>
      <c:forEach items="${workDays}" var="workDay">
        <td><c:out value="${workDay}"/></td>
      </c:forEach>
    </tr>
  </c:if>

  <c:if test="${schedule != null}">
    <c:forEach items="${schedule}" var="stationSchedule">
      <tr>
        <c:forEach items="${stationSchedule}" var="timePeriod">
          <td><c:out value="${timePeriod}"/></td>
        </c:forEach>
      </tr>
    </c:forEach>
  </c:if>

</table>
<br>
<button onclick="topFunction()" id="toTopButton">Наверх</button>
</body>
</html>
