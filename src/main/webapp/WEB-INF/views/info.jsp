<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <script src="${path}/resources/js/info.js"></script>
    <link href="${path}/resources/css/info.css" rel="stylesheet">
    <title>회원정보</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
        <div id="infoform" action="${path}/customer/info.do" method="post">
        	<form id="infoDIV">
        	    <div class="subtitle">가입 정보</div>
				<input id="customer_id" class="input" type="text" name="customer_id" placeholder="아이디" autocomplete="off" value="${sessionScope.customer.customer_id}" disabled>
				<input id="customer_pw_old" class="input" type="password" name="customer_pw_old" placeholder="이전 비밀번호" autocomplete="off" disabled>
        		<input id="customer_pw" class="input" type="password" name="customer_pw" placeholder="새 비밀번호" autocomplete="off" disabled>
            	<input id="customer_pw_check" class="input" type="password" name="customer_pw_check" placeholder="새 비밀번호 확인" autocomplete="off" disabled>
            	<div class="subtitle" style="margin-top:40px;">비밀번호 찾기 질문</div>
            	<select id="password_question_list_id" name ="password_question_list_id" class="input" disabled></select>
            	<input id="password_hint_answer" name ="password_hint_answer" class="input" type="text" autocomplete="off" disabled>
        	    <div id="infoPanel1">
					<input class="button" id="revise_password_button" type="button" value="수정하기">
				</div>
				<div id="infoPanel2" style="display:none">
					<input class="button" id="change_password_button" type="button" value="수정반영하기">
					<input class="button" id="cancel_password_button" type="reset" value="취소하기">
				</div>
        	</form>


        	<form id="otherDIV">
        	    <div class="subtitle">개인 정보</div>
            	<input id="customer_name" class="input" type="text" name="customer_name" placeholder="이름" autocomplete="off" value="${customer.customer_name}" disabled>
            	<input id="customer_bdate" class="input" type="date" name="customer_bdate" placeholder="생일" value="${fn:substring(customer.customer_bdate, 0, 10)}" disabled>
            	<input id="customer_phone" class="input" type="text" name="customer_phone" placeholder="010-XXXX-XXXX" autocomplete="off" value="${customer.customer_phone}" disabled>
            	<input id="customer_address" class="input" type="text" name="customer_address" placeholder="주소" autocomplete="off" value="${customer.customer_address}" disabled>      
        	    <div class="subtitle" style="margin-top:40px;">이메일</div>
        	    <input id="customer_email" class="input" type="text" name="customer_email" placeholder="example@example.com" autocomplete="off" value="${customer.customer_email}" disabled>
           		<input id="email_authcode" class="input" type="text" name="email_authcode" placeholder="인증번호 " autocomplete="off">
           		<div id="email">
           			<input id="email_authcode_button" type="button" value="인증번호 받기">
           			<input id="email_authemail_button" type="button" value="인증하기">
            	</div>
        	    <div id="otherPanel1">
					<input class="button" id="revise_other_button" type="button" value="수정하기">
				</div>
				<div id="otherPanel2" style="display:none">
					<input class="button" id="change_other_button" type="button" value="수정반영하기">
					<input class="button" id="cancel_other_button" type="reset" value="취소하기">
				</div>
        	</form>
        </div>
    </div>
</body>
</html>