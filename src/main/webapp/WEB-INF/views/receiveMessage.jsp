<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <script src="${path}/resources/js/jquery.js"></script>
    <script src="${path}/resources/js/jquery-ui.js"></script>
    <script src="${path}/resources/js/jquery-cookie.js"></script>
    <script src="${path}/resources/js/receiveMessage.js"></script>
    <link href="${path}/resources/css/receiveMessage.css" rel="stylesheet">
    <title>메세지 보관함</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
    	<div id="top_panel">
    		<div id="delete_button">삭제</div>
    		<input id="search" type="text" placeholder="메세지 검색" autocomplete="off" name="message_search">
    		<div id="search_button">검색</div>
    		<div id="message_sent">송신</div>
    		<div id="message_received">수신</div>
    	</div>
    	<table id="list">
    	</table>
    	<div id="paging"></div>
    </div>
</body>
</html>