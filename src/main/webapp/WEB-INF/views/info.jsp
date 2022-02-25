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
    <c:if test= "${sessionScope.CUSTOMER == null}">
    	<script>
    		var form = $("<form action='${path}/customer/mainForm.do' method='post'></form>");
    		$("body").append(form);
    		form.submit();
    	</script>
    </c:if>
    <div id="container" style="overflow: scroll;">
        <div id="infoform" action="${path}/customer/info.do" method="post">
        	<form id="infoDIV">
        	    <div class="subtitle">가입 정보</div>
				<input id="CUSTOMER_ID" class="input" type="text" name="CUSTOMER_ID" placeholder="아이디" autocomplete="off" value="${sessionScope.CUSTOMER.CUSTOMER_ID}" disabled>
				<input id="CUSTOMER_PW_OLD" class="input" type="password" name="CUSTOMER_PW_OLD" placeholder="이전 비밀번호" autocomplete="off" disabled>
        		<input id="CUSTOMER_PW" class="input" type="password" name="CUSTOMER_PW" placeholder="새 비밀번호" autocomplete="off" disabled>
            	<input id="CUSTOMER_PW_CHECK" class="input" type="password" name="CUSTOMER_PW_CHECK" placeholder="새 비밀번호 확인" autocomplete="off" disabled>
            	<div class="subtitle" style="margin-top:40px;">비밀번호 찾기 질문</div>
            	<select name ="PASSWORD_QUESTION_LIST_ID" id="PASSWORD_QUESTION_LIST_ID" class="input" disabled/>
            	<input id="PASSWORD_HINT_ANSWER" name ="PASSWORD_HINT_ANSWER" class="input" type="text" autocomplete="off" disabled>
        	    <div id="infoPanel1">
					<input class="button" id="REVISE_PASSWORD_BUTTON" type="button" value="수정하기">
				</div>
				<div id="infoPanel2" style="display:none">
					<input class="button" id="CHANGE_PASSWORD_BUTTON" type="button" value="수정반영하기">
					<input class="button" id="CANCEL_PASSWORD_BUTTON" type="reset" value="취소하기">
				</div>
        	</form>


        	<form id="otherDIV">
        	    <div class="subtitle">개인 정보</div>
            	<input id="CUSTOMER_NAME" class="input" type="text" name="CUSTOMER_NAME" placeholder="이름" autocomplete="off" value="${CUSTOMER.CUSTOMER_NAME}" disabled>
            	<input id="CUSTOMER_BDATE" class="input" type="date" name="CUSTOMER_BDATE" placeholder="생일" value="${fn:substring(CUSTOMER.CUSTOMER_BDATE, 0, 10)}" disabled>
            	<input id="CUSTOMER_PHONE" class="input" type="text" name="CUSTOMER_PHONE" placeholder="010-XXXX-XXXX" autocomplete="off" value="${CUSTOMER.CUSTOMER_PHONE}" disabled>
            	<input id="CUSTOMER_ADDRESS" class="input" type="text" name="CUSTOMER_ADDRESS" placeholder="주소" autocomplete="off" value="${CUSTOMER.CUSTOMER_ADDRESS}" disabled>      
        	    <div class="subtitle" style="margin-top:40px;">이메일</div>
        	    <input id="CUSTOMER_EMAIL" class="input" type="text" name="CUSTOMER_EMAIL" placeholder="example@example.com" autocomplete="off" value="${CUSTOMER.CUSTOMER_EMAIL}" disabled>
           		<input id="EMAIL_AUTHCODE" class="input" type="text" name="EMAIL_AUTHCODE" placeholder="인증번호 " autocomplete="off">
           		<div id="EMAIL">
           			<input id="EMAIL_AUTHCODE_BUTTON" type="button" value="인증번호 받기">
           			<input id="EMAIL_AUTHEMAIL_BUTTON" type="button" value="인증하기">
            	</div>
        	    <div id="otherPanel1">
					<input class="button" id="REVISE_OTHER_BUTTON" type="button" value="수정하기">
				</div>
				<div id="otherPanel2" style="display:none">
					<input class="button" id="CHANGE_OTHER_BUTTON" type="button" value="수정반영하기">
					<input class="button" id="CANCEL_OTHER_BUTTON" type="reset" value="취소하기">
				</div>
        	</form>
        </div>
    </div>
</body>
</html>