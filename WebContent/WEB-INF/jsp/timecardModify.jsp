<%@page import="java.util.Date"%>
<%@page import="dao.TimeCard"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String date = (String)request.getAttribute("date");
    String arrivalTime = (String)request.getAttribute("arrivalTime");
    String leaveTime = (String)request.getAttribute("leaveTime");
    String goOutTime = (String)request.getAttribute("goOutTime");
    String goBackTime = (String)request.getAttribute("goBackTime");
    String timecardID = (String)request.getAttribute("timecardID");
    %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/timecardModify.css" type="text/css">
<script type="text/javascript" src="javascript/header.js"></script>
<meta charset="UTF-8">
<title>タイムカード修正</title>
</head>
<body onload="changeHeader()">
<div id="header">
</div>
<div class="modify">
<h1>${user.name}さんのタイムカードの修正</h1>

<form action="/SVD_IntraNet/TimeCardModify" method="post">
<table class="modifyTable">
<tr><th class="date">日付</th>
<th class="input">開始時間</th>
<th class="input">終了時間</th></tr>
<tr><td class="date"><%=date %></td>
<td class="input">
<%=arrivalTime %>
</td>
<td class="input">
<%=leaveTime %>
</td>
</tr>
</table>
<input type="hidden" name="timecardID" value="<%=timecardID%>">
<input type="hidden" name="date" value="<%=date%>">

<div class="submit">
<input type="submit" value="登録">
<input type="reset" value="入力を取り消す">
</div>
</form>
</div>
</body>
</html>