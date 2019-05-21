<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String weekly = (String)session.getAttribute("personalWeek");
    String calendar = (String)session.getAttribute("personalMonthView");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップページ - Star View Data</title>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/default.css" type="text/css" media="screen and (min-width:640px)">
<link rel="stylesheet" href="css/defaultForSmartphone.css" type="text/css" media="screen and (max-width:640px)">
<script type="text/javascript" src="javascript/header.js"></script>
</head>
<body onload="changeHeader()">
<div id="header">
</div>

<div class="top">
<p>${user.name}さんのスケジュール</p>
<%= weekly%>
</div>
<div class="left">
<p>カレンダー</p>
<%=calendar %>
</div>

<jsp:include page="/jsp/comment_bar.jsp"></jsp:include>

</body>
</html>