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
    <script src="${path}/resources/js/sendMessage.js"></script>
    <link href="${path}/resources/css/sendMessage.css" rel="stylesheet">
    <title>메세지 보내기</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"></jsp:include>
    <div id="container" style="overflow: scroll;">
		<div id="message_send">
			<div class="subtitle">메세지를 받을 사람</div>
			<input id="customer_id" class="input" type="text" autocomplete="off" placeholder="받을 사람의 ID">
			<div style="margin-top:40px;" class="subtitle">메세지 제목</div>
			<input id="message_title" class="input" type="text" autocomplete="off" maxlength="300" placeholder="메세지 제목">
			<div style="margin-top:40px;" class="subtitle">메세지 내용</div>
			<textarea id="message_content" class="input" autocomplete="off" maxlength="3000" placeholder="메세지 내용"></textarea>
			<input id="message_send_button" type="button" value="메세지 보내기">
		</div>
    </div>
</body>
</html>