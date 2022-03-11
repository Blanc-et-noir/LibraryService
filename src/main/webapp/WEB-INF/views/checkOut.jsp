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
    <script src="${contextPath}/resources/js/checkOut.js"></script>
    <link href="${contextPath}/resources/css/checkOut.css" rel="stylesheet">
    <title>대출하기</title>
</head>
<body>
    <div id="container" style="overflow: scroll;">
        <form id="checkOutform">
			<input id="CUSTOMER_ID" type="text" name="customer_id" autocomplete="off" placeholder="사용자 ID" class="input">
			<input id="BOOK_ISBN" type="text" name="book_isbn" autocomplete="off" placeholder="ISBN 코드" class="input">
            <div style="display: flex; flex-direction: row; width: 100%; flex: 1;">
				<input id="CHECK_OUT_BUTTON" type="button" value="대출하기">
				<input id="CHECK_OUT_RESET_BUTTON" type="reset" value="새로고침">
            </div>
        </form>
    </div>
</body>
</html>