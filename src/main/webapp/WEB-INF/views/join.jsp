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
    <script src="${path}/resources/js/JSEncrypt.js"></script>
    <script src="${path}/resources/js/join.js"></script>
    <link href="${path}/resources/css/join.css" rel="stylesheet">
    <title>회원가입</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
        <form id="joinform" action="${path}/customer/join.do" method="post">
        	<div class="subtitle">가입 정보</div>
			<input id="customer_id" class="input" type="text" name="customer_id" placeholder="아이디" autocomplete="off">
        	<input id="customer_pw" class="input" type="password" name="customer_pw" placeholder="비밀번호" autocomplete="off">
            <input id="customer_pw_check" class="input" type="password" name="customer_pw_check" placeholder="비밀번호 확인" autocomplete="off">
        	
        	<div class="subtitle" style="margin-top:40px;">비밀번호 찾기 질문</div>
            <select id="password_question_list_id" name ="password_question_list_id" class="input"/>
            <input id="password_hint_answer" name ="password_hint_answer" class="input" type="text" autocomplete="off">
        	
        	<div class="subtitle" style="margin-top:40px;">개인 정보</div>
            <input id="customer_name" class="input" type="text" name="customer_name" placeholder="이름" autocomplete="off">
            <input id="customer_bdate" class="input" type="date" name="customer_bdate" placeholder="생일">
            <input id="customer_phone" class="input" type="text" name="customer_phone" placeholder="010-XXXX-XXXX" autocomplete="off">
            <input id="customer_addres" class="input" type="text" name="customer_addres" placeholder="주소" autocomplete="off">    
            
            <div class="subtitle" style="margin-top:40px;">이메일</div>
            <input id="customer_email" class="input" type="text" name="customer_email" placeholder="example@example.com" autocomplete="off">
            <input id="email_authcode" class="input" type="text" name="email_authcode" placeholder="인증번호 " autocomplete="off">
            <div id="email">
           		<input id="email_authcode_button" type="button" value="인증번호 받기">
           		<input id="email_authemail_button" type="button" value="인증하기">
            </div>  
            <input id="join_button" type="button" value="회원가입">
        </form>
    </div>
</body>
</html>