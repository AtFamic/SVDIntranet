<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	String weekly = (String)session.getAttribute("weekly");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール（週表示）</title>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/general_style.css" type="text/css">
<link rel="stylesheet" href="css/weekly.css" type="text/css">
<script type="text/javascript" src="javascript/header.js"></script>
</head>
<body onload="changeHeader()">
<div id="header">
</div>

<div class="content">
<p><span class="anchor"><a href="/SVD_IntraNet/ScheduleServlet"><img alt="Plus" src="img/plus.png">予定を登録する</a></span></p>
<div class="changeTable">
<span id="MonthlyView"><a href="/SVD_IntraNet/MonthViewServlet?isInitial=true">月表示</a></span>
<span id="WeeklyView"><a href="/SVD_IntraNet/WeeklyViewServlet">週表示</a></span>
</div>
<%=weekly %>
</div>

</body>
</html>