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
    <script src="${path}/resources/js/board.js"></script>
    <link rel="stylesheet" href="${path}/resources/css/board.css">
    <title>
    	<c:choose>
    		<c:when test="${BOARD=='free_board'}">자유게시판</c:when>
    		<c:when test="${BOARD=='qna_board'}">문의게시판</c:when>
    		<c:when test="${BOARD=='info_board'}">정보게시판</c:when>
    	</c:choose>
    </title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
    	<div>
    		<div id="panel">
    			<select id="FLAG" name="flag">
    				<option value="CUSTOMER_ID" selected>작성자</option>
    				<option value="ARTICLE_TITLE">제목</option>
    				<option value="ARTICLE_CONTENT">내용</option>
    			</select>
    			<input id="SEARCH" type="text" name="data">
    			<input id="SEARCH_BUTTON" type="button" value="검색">
    		</div>
    		<table id="listTable" value="${BOARD}">
    		</table>
   		</div>
   		<div id="paging"></div>
		<input id="write" type="button" value="글 쓰기">
    </div>
</body>
</html>