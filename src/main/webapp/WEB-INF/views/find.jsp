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
    <script src="${path}/resources/js/find.js"></script>
    <link href="${path}/resources/css/find.css" rel="stylesheet">
    <title>아이디/비밀번호 찾기</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"></jsp:include>
    <div id="container" style="overflow: scroll;">
        <form id="findform" action="${path}/customer/find.do" method="post">
        	<div style="display:flex; margin-bottom:40px; flex-direction:row;">
        		<div id="find_id">아이디 찾기</div>
        		<div id="find_pw">비밀번호 찾기</div>
        	</div>
        	<div id="find_id_block">
        		<div class="subtitle">전화번호로 아이디 찾기</div>
        		<div style="display:flex; flex-direction:row;">
        	    	<input id="customer_phone" style="width:85%;" class="input" type="text" name="customer_phone" placeholder="- 포함 전화번호" autocomplete="off">
            		<input id="find_by_phone" type="button" value="찾기">
        		</div>
        		<div class="subtitle" style="margin-top:40px;">이메일로 아이디 찾기</div>
        		<div style="display:flex; flex-direction:row;">
        			<input id="customer_email" style="width:85%;" class="input" type="text" name="customer_email" placeholder="이메일 주소" autocomplete="off">
            		<input id="find_by_email" type="button" value="찾기">
        		</div>
        	</div>
        	<div id="find_pw_block" style="display:none;">
        	    <div class="subtitle">비밀번호 찾기</div>
        		<div style="display:flex; flex-direction:row;">
        			<input id="customer_id" style="width:85%;" class="input" type="text" name="customer_id" placeholder="비밀번호를 찾고자 하는 아이디" autocomplete="off">
        			<input id="get_question_button" type="button" value="확인">
        		</div>
        		<div class="subtitle" style="margin-top:40px;">비밀번호 찾기 질문</div>
        		<p id="password_question_list_content"></p>
        		<div style="display:flex; flex-direction:row;">
        			<input id="password_hint_answer" style="width:85%;" class="input" type="text" name="password_hint_answer" placeholder="비밀번호 찾기 질문에 대한 답" autocomplete="off">
            		<input id="find_password_button" type="button" value="찾기">
        		</div>
        	</div>
        </form>
    </div>
</body>
</html>