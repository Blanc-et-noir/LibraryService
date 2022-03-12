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
    			<select id="flag" name="flag">
    				<option value="customer_id" selected>사용자 ID</option>
    				<option value="customer_name">사용자 이름</option>
    				<option value="book_isbn">ISBN 코드</option>
    				<option value="book_name">책 제목</option>
    			</select>
    			<input id="start_date" type="date">
    			<p class="sub" style="width:60px"> ~ </p>
    			<input id="end_date" type="date">
    			<input id="search" type="text" name="search">
    			<input id="search_button" type="button" value="검색">
    		</div>
    		<table id="listTable">
    		</table>
   		</div>
   		<div id="paging"></div>
    </div>
</body>
</html>