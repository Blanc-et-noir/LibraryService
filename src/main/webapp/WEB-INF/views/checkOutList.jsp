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
    <script src="${path}/resources/js/checkOutList.js"></script>
    <link rel="stylesheet" href="${path}/resources/css/checkOutList.css">
    <title>대출 현황</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
    	<div>
    		<div id="panel">
    			<select id="FLAG" name="flag">
    				<option value="CUSTOMER_ID" selected>사용자 ID</option>
    				<option value="CUSTOMER_NAME">사용자 이름</option>
    				<option value="BOOK_ISBN">ISBN 코드</option>
    				<option value="BOOK_NAME">책 제목</option>
    			</select>
    			<input id="START_DATE" type="date">
    			<p class="sub" style="width:60px"> ~ </p>
    			<input id="END_DATE" type="date">
    			<input id="SEARCH" type="text" name="data">
    			<input id="SEARCH_BUTTON" type="button" value="검색">
    		</div>
    		<table id="listTable">
    		</table>
   		</div>
   		<div id="paging"></div>
    </div>
</body>
</html>