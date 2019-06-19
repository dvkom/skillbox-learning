<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
  <%
    TreeSet<String> workDays = (TreeSet<String>) request.getAttribute("workDays");
    if (workDays != null) {
    out.println("<tr class=\"w3-blue\">");
    out.println("<td>Участок</td>");
    for (String s : workDays) {
      out.println("<td>" + s + "</td>");
    }
    out.println("</tr>");
    Map<Integer, Map<String, String>> voteStationsSchedule =
        (Map<Integer, Map<String, String>>) request.getAttribute("voteStationsSchedule");
    if (voteStationsSchedule != null) {
      for (Map.Entry<Integer, Map<String, String>> entry : voteStationsSchedule.entrySet()) {
        out.println("<tr>");
        out.println("<td>" + entry.getKey() + "</td>");
        for (String day : workDays) {
          if (entry.getValue().containsKey(day)) {
            out.println("<td>" + entry.getValue().get(day) + "</td>");
          } else {
            out.println("<td> - </td>");
          }
        }
      }
    }
      out.println("</tr>");
    }
  %>
</table>
<br>
<button onclick="topFunction()" id="toTopButton">Наверх</button>
</body>
</html>
