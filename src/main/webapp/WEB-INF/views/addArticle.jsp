<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <script src="${contextPath}/resources/js/jquery.js"></script>
    <script src="${contextPath}/resources/js/jquery-ui.js"></script>
    <script src="${contextPath}/resources/js/jquery-cookie.js"></script>
    <script src="${contextPath}/resources/js/JSEncrypt.js"></script>
    <script src="${contextPath}/resources/js/addArticle.js"></script>
    <link href="${contextPath}/resources/css/addArticle.css" rel="stylesheet">
    <title>글 쓰기</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
        <form id="addArticleform" action="/LibraryService/board/addArticle.do?BOARD=${BOARD}" method="post" enctype="multipart/form-data">
			<p class="subtitle">제목</p>
			<input id="ARTICLE_TITLE" type="text" name="ARTICLE_TITLE">
			<p class="subtitle">내용</p>
			<textarea id="ARTICLE_CONTENT" name="ARTICLE_CONTENT"></textarea>
			<p class="subtitle">파일</p>
			<div id="FILE">
			</div>
        	<div id="control">
        		<input id="FILE_ADD_BUTTON" type="button" value="파일 추가">
        		<input id="return" type="button" value="목록보기">
        		<input id="addArticle" type="button" value="글 쓰기">
        	</div>
        	<input id="BOARD" type="hidden" name="BOARD" value="${BOARD}">
        	<input id="PARENT_ARTICLE_ID" type="hidden" name="PARENT_ARTICLE_ID" value="${PARENT_ARTICLE_ID}">
        </form>
    </div>
</body>
</html>