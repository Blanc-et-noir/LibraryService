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
    <div id="container" style="overflow: scroll;">
		<div id="message_send">
			<div class="subtitle">
				<c:choose>
					<c:when test="${message_box == 'message_received'}">
						송신자
					</c:when>
					<c:otherwise>
						수신자
					</c:otherwise>
				</c:choose>
			</div>
			<c:choose>
				<c:when test="${message_box == 'message_received'}">
					<input id="customer_id" class="input" type="text" autocomplete="off" readonly value="${message.sender_id}">
				</c:when>
				<c:otherwise>
					<input id="customer_id" class="input" type="text" autocomplete="off" readonly value="${message.receiver_id}">
				</c:otherwise>
			</c:choose>
			<div style="margin-top:40px;" class="subtitle">메세지 제목</div>
			<input id="message_title" class="input" type="text" autocomplete="off" readonly value="${message.message_title}">
			<div style="margin-top:40px;" class="subtitle">메세지 내용</div>
			<textarea id="message_content" class="input" autocomplete="off" maxlength="1000" placeholder="메세지 내용" readonly>${message.message_content}</textarea>
			<form id="bottom_panel" action="/LibraryService/message/deleteMessage.do">
				<input id="message_id" name="message_id" type="hidden" value="${message.message_id}">
				<input id="message_box" name="message_box" type="hidden" value="${message_box}">
				<input id="message_return_button" type="button" value="목륵보기">
				<input id="message_delete_button" type="button" value="삭제하기">
			</form>
		</div>
		<div>${message_box}dd</div>
    </div>
</body>
</html>