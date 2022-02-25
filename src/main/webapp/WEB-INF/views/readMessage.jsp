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
    <script src="${path}/resources/js/readMessage.js"></script>
    <link href="${path}/resources/css/readMessage.css" rel="stylesheet">
    <title>메세지 읽기</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"></jsp:include>
	<c:if test= "${sessionScope.CUSTOMER == null}">
    	<script>
    		var form = $("<form action='${path}/customer/mainForm.do' method='post'></form>");
    		$("body").append(form);
    		form.submit();
    	</script>
    </c:if>
    <div id="container" style="overflow: scroll;">
		<div id="MESSAGE_SEND">
			<div class="subtitle">
				<c:choose>
					<c:when test="${MESSAGE_BOX == 'MESSAGE_RECEIVED'}">
						송신자
					</c:when>
					<c:otherwise>
						수신자
					</c:otherwise>
				</c:choose>
			</div>
			<c:choose>
				<c:when test="${MESSAGE_BOX == 'MESSAGE_RECEIVED'}">
					<input id="CUSTOMER_ID" class="input" type="text" autocomplete="off" readonly value="${messageVO.SENDER_ID}">
				</c:when>
				<c:otherwise>
					<input id="CUSTOMER_ID" class="input" type="text" autocomplete="off" readonly value="${messageVO.RECEIVER_ID}">
				</c:otherwise>
			</c:choose>
			<div style="margin-top:40px;" class="subtitle">메세지 제목</div>
			<input id="MESSAGE_TITLE" class="input" type="text" autocomplete="off" readonly value="${messageVO.MESSAGE_TITLE}">
			<div style="margin-top:40px;" class="subtitle">메세지 내용</div>
			<textarea id="MESSAGE_CONTENT" class="input" autocomplete="off" maxlength="1000" placeholder="메세지 내용" readonly>${messageVO.MESSAGE_CONTENT}</textarea>
			<form id="BOTTOM_PANEL" action="/LibraryService/message/deleteMessage.do">
				<input id="MESSAGE_ID" name="MESSAGE_ID" type="hidden" value="${messageVO.MESSAGE_ID}">
				<input id="MESSAGE_BOX" name="MESSAGE_BOX" type="hidden" value="${MESSAGE_BOX}">
				<input id="MESSAGE_RETURN_BUTTON" type="button" value="목륵보기">
				<input id="MESSAGE_DELETE_BUTTON" type="button" value="삭제하기">
			</form>
		</div>
    </div>
</body>
</html>