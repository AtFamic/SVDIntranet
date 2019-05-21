<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen and (min-width:640px)">
<link rel="stylesheet" type="text/css" href="css/login.css" media="screen and (min-width: 640px)">
<link rel="stylesheet" type="text/css" href="css/loginForSmartphone.css" media="screen and (max-width: 640px)">
<script type="text/javascript" src="javascript/test.js"></script>
<title>スタービュー・データ株式会社　社内イントラネット</title>
</head>
<body onload="changeIMG()">
<!-- content -->
<div id="content">
<div class="relative">
  <img src="img/header.png" class="header">
  <div id="img1">
  </div>
</div>
</div>
      <!-- loginArea -->
      <div class="loginArea">
      <form action="/SVD_IntraNet/LoginServlet" method="post">
      <h1>スタービュー・データ株式会社</h1>
      <div class="input"><input name="userID" type="text" tabindex="1" class="ime-inactive" placeholder="UserID" /></div>
      <div class="input"><input name="password" type="password" tabindex="2" class="ime-inactive"  placeholder="Password"/></div>
      <input type="submit" name="btnLogin" value="Login" id="btnLogin" class="btred" />
      <c:if test="${loginFalt == true}">
      <p>ログインに失敗しました。<br>もう一度入力してください。</p>
      </c:if>
      </form>
      </div>
      <!--  end_LoginArea  -->
</body>
</html>