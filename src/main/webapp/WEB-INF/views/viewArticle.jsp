<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<c:set var="path" value="${pageContext.request.contextPath}"/>
    <script src="${path}/resources/js/jquery.js"></script>
    <script src="${path}/resources/js/jquery-ui.js"></script>
    <script src="${path}/resources/js/jquery-cookie.js"></script>
    <script src="${path}/resources/js/JSEncrypt.js"></script>
    <script src="${path}/resources/js/viewArticle.js"></script>
    <link rel="stylesheet" href="${path}/resources/css/viewArticle.css">
    <title>글 읽기</title>
    <c:set var="articleVO" value="${requestScope.articleVO}"/>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;" class="t3">
    	<c:choose>
    		<c:when  test="${not empty articleVO}">
    			<form id="viewArticle" action="/LibraryService/board/modArticle.do?BOARD=${BOARD}" method="post" enctype="multipart/form-data">
        			<input type="hidden" name="ORIGINAL_ARTICLE_ID" value="${articleVO.ARTICLE_ID}">
        			<input id="ARTICLE_ID" type="hidden" name="ARTICLE_ID" value="${articleVO.ARTICLE_ID}">
        			<input type="hidden" name="CUSTOMER_ID" value="${articleVO.CUSTOMER_ID}">
        			<input id="BOARD" type="hidden" name="BOARD" value="${BOARD}">
					<input id="CUSTOMER_ID" type="text" value="${articleVO.CUSTOMER_ID}" disabled>
					<p id="ARTICLE_DATE">${articleVO.ARTICLE_DATE_STRING}</p>
					
					<p class="subtitle">제목</p>
					<input id="ARTICLE_TITLE" class="v input" type="text" name="ARTICLE_TITLE" value="${articleVO.ARTICLE_TITLE}" disabled>
					<p class="subtitle">내용</p>
					<textarea id="ARTICLE_CONTENT" class="v input" name="ARTICLE_CONTENT" disabled>${articleVO.ARTICLE_CONTENT}</textarea>
					<div id="FILE">
						<c:if test="${not empty FILES}">
							<c:forEach items="${FILES}" var="file" varStatus="status">
								<div class="fileList">
									<img name='file${status.index}' class="image" src="${path}/board/download.do?BOARD=${BOARD}&ARTICLE_ID=${file.ARTICLE_ID}&FILE_TEMP_NAME=${file.FILE_TEMP_NAME}">
									<input id="file${status.index}" class="v" type="file" name="file${status.index}" onchange="changeFile(this); preview(this);" style="display:none;"disabled>
									<input class="FILE_TEMP_NAME" type="hidden" name="FILE_TEMP_NAME" value="${file.FILE_TEMP_NAME}">
									<label class='label' for="file${status.index}"></label>
									<input type="button" value="✖" class="v remove" onclick="removeFile(this);"disabled>
								</div>
							</c:forEach>
						</c:if>
					</div>
        			<div id="control1">
        				<input id="return" type="button" value="목록보기">
        				<input id="reply" type="button" value="답글쓰기">
        				<c:if test="${(not empty sessionScope.CUSTOMER)&&sessionScope.CUSTOMER.CUSTOMER_ID ==  articleVO.CUSTOMER_ID}">
        					<input id="revise" type="button" value="수정하기">
        					<input id="remove" type="button" value="삭제하기">
        				</c:if>
        			</div>
        			<div id="control2" style="display:none;">
        				<input id="FILE_ADD_BUTTON" type="button" value="파일추가" style="display:none">
        				<input id="confirm" type="button" value="완료하기">
        				<input id="cancel" type="button" value="취소하기">
        			</div>
        		</form>
    		</c:when>
    		<c:otherwise>
    			<p id="error">해당 게시글 정보가 존재하지 않습니다.</p>
    		</c:otherwise>
    	</c:choose>
    </div>
</body>
</html>