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
    <c:if test= "${sessionScope.CUSTOMER == null}">
    	<script>
    		var form = $("<form action='${path}/customer/mainForm.do' method='post'></form>");
    		$("body").append(form);
    		form.submit();
    	</script>
    </c:if>
    <div id="container" style="overflow: scroll;">
    	<div id="TOP_PANEL">
    		<div id="DELETE_BUTTON">삭제</div>
    		<input id="SEARCH"type="text" placeholder="메세지 검색" autocomplete="off" name="MESSAGE_SEARCH">
    		<div id="SEARCH_BUTTON">검색</div>
    		<div id="MESSAGE_SENT">송신</div>
    		<div id="MESSAGE_RECEIVED">수신</div>
    	</div>
    	<table id="LIST">
    	</table>
    	<div id="paging"></div>
    </div>
</body>
</html>