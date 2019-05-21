<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String allAccountsSelect = (String)session.getAttribute("allAccountsSelect");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>コメント登録</title>
<link rel="stylesheet" href="css/navigation3.css" type="text/css">
<link rel="stylesheet" href="css/information.css" type="text/css">
<script type="text/javascript" src="javascript/header.js"></script>
</head>
<body onload="changeHeader()">
<div id="header">
</div>
<div class="left">
	<div class="informatiion">
	<span class="title>"><p>メッセージの登録</p></span>
	<form action="/SVD_IntraNet/InformationServlet" method="post">
		<div class="input"><p>タイトル<br><input type="text" class="input" name="title" required></p></div>
		<div class="input"><p>送信先<br><%=allAccountsSelect %></p></div>
		<div class="content"><p>コメント<br><textarea class="contents" rows="3" name="content" required></textarea></p></div>
		<div class="submit">
		<input type="submit" value="登録">
		<input type="reset" value="入力を取り消す">
		</div>
	</form>
	</div>
</div>
</body>
</html>