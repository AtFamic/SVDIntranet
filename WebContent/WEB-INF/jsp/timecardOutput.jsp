<%@page import="util.TimecardUtil"%>
<%@page import="java.util.Date"%>
<%@page import="dao.TimeCard"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	String sYear = TimecardUtil.getYearHTML("startYear");
        String sMonth = TimecardUtil.getMonthHTML("startMonth");
        String sDate = TimecardUtil.getDateHTML("startDate");
        String eYear = TimecardUtil.getYearHTML("endYear");
        String eMonth = TimecardUtil.getMonthHTML("endMonth");
        String eDate = TimecardUtil.getDateHTML("endDate");
        String file_path = request.getParameter("file_path");
    %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/timecardModify.css" type="text/css">
<script type="text/javascript" src="javascript/header.js"></script>
<meta charset="UTF-8">
<title>タイムカードの出力</title>
</head>
<body onload="changeHeader()">
<div id="header">
</div>
<div class="modify">
<h1>${user.name}さんのタイムカードの出力</h1>

<form action="/SVD_IntraNet/TimecardOutputServlet" method="post">
<table class="modifyTable">
<tr>
<th class="input">開始日</th>
<th class="input">終了日</th></tr>
<tr>
<td class="input">
<%=sYear %>
<%= sMonth %>
<%=sDate %>
</td>
<td class="input">
<%=eYear %>
<%= eMonth %>
<%=eDate %>
</td>
</tr>
</table>
<div class="submit">
<input type="submit" value="出力">
<input type="reset" value="入力を取り消す">
</div>
</form>
</div>
<div id="error">
<c:choose>
	<c:when test="${isError}"><p>開始日は終了日よりも早くできません。</p></c:when>
</c:choose>
</div>
</body>
</html>