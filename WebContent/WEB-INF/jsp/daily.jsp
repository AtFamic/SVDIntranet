<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	String table = (String)request.getAttribute("dailyTable");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>指定日スケジュール</title>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/general_style.css" type="text/css">
<link rel="stylesheet" href="css/daily.css" type="text/css">
</head>

<body>
<jsp:include page="/jsp/header.jsp"></jsp:include>

<div class="left">
	<%=table %>
</div>
</body>
</html>