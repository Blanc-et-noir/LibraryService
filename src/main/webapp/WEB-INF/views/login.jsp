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
    <script src="${path}/resources/js/login.js"></script>
    <link href="${path}/resources/css/login.css" rel="stylesheet">
    <title>로그인</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/sidebar.jsp"/>
    <div id="container" style="overflow: scroll;">
        <form id="loginform" action="/LibraryService/login" method="post">
            <input id="customer_id" class="input" type="text" name="customer_id" placeholder="아이디" autocomplete="off">
            <input id="customer_pw" class="input" type="password" name="customer_pw" placeholder="비밀번호" autocomplete="off">
            <input id="login_button" type="button" value="로그인">
            <div style="display: flex; flex-direction: row; width: 100%; flex: 1; margin-top:10px;">
                <a href="${path}/customer/findForm.do">아이디/비밀번호 찾기</a>
                <a href="${path}/customer/joinForm.do">회원가입</a>
            </div>
        </form>
    </div>
</body>
</html>