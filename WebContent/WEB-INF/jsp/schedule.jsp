<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
Date today = new Date();
Calendar calendar = Calendar.getInstance();
calendar.setTime(today);
int year = calendar.get(Calendar.YEAR);
int month = calendar.get(Calendar.MONTH) + 1;
int date = calendar.get(Calendar.DATE);

String initialDate = "";
if(month < 10){
	if(date < 10){
		initialDate = String.valueOf(year) + "-0" + String.valueOf(month) + "-0" + String.valueOf(date);
	}else{
		initialDate = String.valueOf(year) + "-0" + String.valueOf(month) + "-" + String.valueOf(date);
	}
}else{
	if(date < 10){
		initialDate = String.valueOf(year) + "-" + String.valueOf(month) + "-0" + String.valueOf(date);
	}else{
		initialDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(date);
	}
}

String timeChoosed = (String)request.getAttribute("timeChoosed");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール登録・編集</title>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/general_style.css" type="text/css">
<link rel="stylesheet" href="css/schedule.css" type="text/css">
<script type="text/javascript" src="javascript/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="javascript/schedule.js"></script>
<script type="text/javascript" src="javascript/header.js"></script>
</head>
<body onload="changeHeader()">
<div id="header">
</div>

	<div id="caption">
	<h1>予定の登録</h1>
	</div>

	<div class="schedule">
	<c:choose>
		<c:when test="${task.taskID == null }">
			<form action="/SVD_IntraNet/ScheduleServlet" method="post">
		</c:when>
		<c:when test="${task.taskID != null}">
			<form action="/SVD_IntraNet/ScheduleServlet?taskID=${task.taskID}" method="post">
		</c:when>
	</c:choose>
		<p>日付
		<label class="date">
	<c:choose>
	<c:when test="${timeChoosed == null}">
		<input type="date" name="startDate" min="20000101" max="21001231" value="<%=initialDate %>" required>
	</c:when>
	<c:when test="${timeChoosed != null}">
		<input type="date" name="startDate" min="20000101" max="21001231" value="<%=timeChoosed %>" required>
	</c:when>
	</c:choose>
		</label>
		</p>
		<p id="time">時刻
		<label class="time">
		<select name="startHour">
		<option value="">--時</option>
		<option value="0">0時</option>
		<option value="1">1時</option>
		<option value="2">2時</option>
		<option value="3">3時</option>
		<option value="4">4時</option>
		<option value="5">5時</option>
		<option value="6">6時</option>
		<option value="7">7時</option>
		<option value="8">8時</option>
		<option value="9">9時</option>
		<option value="10">10時</option>
		<option value="11">11時</option>
		<option value="12">12時</option>
		<option value="13">13時</option>
		<option value="14">14時</option>
		<option value="15">15時</option>
		<option value="16">16時</option>
		<option value="17">17時</option>
		<option value="18">18時</option>
		<option value="19">19時</option>
		<option value="20">20時</option>
		<option value="21">21時</option>
		<option value="22">22時</option>
		<option value="23">23時</option>
		</select>
		<select name="startMin">
		<option value="">--分</option>
		<option value="0">0分</option>
		<option value="30">30分</option>
		</select>
		～
		<select name="endHour">
		<option value="">--時</option>
		<option value="0">0時</option>
		<option value="1">1時</option>
		<option value="2">2時</option>
		<option value="3">3時</option>
		<option value="4">4時</option>
		<option value="5">5時</option>
		<option value="6">6時</option>
		<option value="7">7時</option>
		<option value="8">8時</option>
		<option value="9">9時</option>
		<option value="10">10時</option>
		<option value="11">11時</option>
		<option value="12">12時</option>
		<option value="13">13時</option>
		<option value="14">14時</option>
		<option value="15">15時</option>
		<option value="16">16時</option>
		<option value="17">17時</option>
		<option value="18">18時</option>
		<option value="19">19時</option>
		<option value="20">20時</option>
		<option value="21">21時</option>
		<option value="22">22時</option>
		<option value="23">23時</option>
		</select>
		<select name="endMin">
		<option value="">--分</option>
		<option value="0">0分</option>
		<option value="30">30分</option>
		</select>
		</label>
		</p>
		<p>予定
		<select name="color" class="color" required>
		<option value="" selected>--</option>
		<option value="往訪" class="ouhou">往訪</option>
		<option value="作業" class="sagyou">作業</option>
		<option value="出張" class="shucchou">出張</option>
		<option value="会議" class="shussha">会議</option>
		<option value="来訪" class="raihou">来訪</option>
		<option value="休み" class="yasumi">休み</option>
		</select>
		<c:choose>
		<c:when test="${task.title == null }">
			<input type="text" class="input" name="title" required>
		</c:when>
		<c:when test="${task.taskID != null}">
			<input type="text" class="input" name="title" value= "${task.title}" required>
		</c:when>
		</c:choose>
		</p>
		<p>メモ<br>
		<c:choose>
		<c:when test="${task.content == null }">
			<textarea  class="contents" rows="3" name="content" ></textarea>
		</c:when>
		<c:when test="${task.content != null}">
			<textarea  class="contents" rows="3" name="content">${taks.content}</textarea>
		</c:when>
		</c:choose>
		</p>
		<p>
		公開/非公開<input type="radio" name="isPublic" checked="checked">公開
		<input type="radio" name="isPublic" value="false">非公開
		</p>
		<div class="submit">
		<p>
		<input type="submit" value="登録" onClick="return checkTime()">
		<input type="reset" value="入力を取り消す">
		</p>
		</div>
	</form>
	</div>
</body>
</html>