<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="dao.*" %>
    <%@ page import="java.util.*" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%


    String publicInformaiton = (String)session.getAttribute("publicInformation");
    String personalInformation = (String)session.getAttribute("personalInformation");
    %>
<div class="right">
	<div class="public message">
	<p>お知らせ　【会社全体宛】</p>
	<%=publicInformaiton %>
	</div>

	<div class="personal message">
	<p>お知らせ　【${user.name}さん宛】</p>
	<%=personalInformation %>
	</div>

</div>