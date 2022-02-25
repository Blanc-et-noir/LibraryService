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
    <script src="${path}/resources/js/sidebar.js"></script>
    <link href="${path}/resources/css/sidebar.css" rel="stylesheet">
    <title>사이드바</title>
</head>
<body>
	<div id="cover"></div>
	<div id="sidebar">
		<div id="sidebarbutton">☰</div>
			<div id="sideContent">
		        <div id="myinfo">
            	<c:choose>
            		<c:when test= "${sessionScope.CUSTOMER != null}">
            			<div id="CUSTOMER_INFO">${sessionScope.CUSTOMER.CUSTOMER_NAME}</div>
						<a id="logout" href="${path}/customer/logout.do">로그아웃</a>
            		</c:when>
            		<c:otherwise>
            			<a id="join" href="${path}/customer/joinForm.do">회원가입</a>
            			<a id="login" href="${path}/customer/loginForm.do">로그인</a>
            		</c:otherwise>
            	</c:choose>
        	</div>
        	<div id="search">
            	<input type="text" name="bookname" id="inputname" placeholder="도서 또는 작가 이름을 입력하세요" autocomplete="off">
        	</div>
        	<div id="nav">
            	<a class="mainnav" href="${path}/customer/mainForm.do">홈 화면</a>
            	<c:if test="${sessionScope.CUSTOMER !=null and sessionScope.CUSTOMER.getKIND_NUMBER()==0}">
					<div id="mainnav1" class="mainnav active">사용자 관리</div>
            		<div class="subnav">
            			<!--<a class="option" href="${path}/book/checkOutForm.do">도서 대출</a>-->
                		<a class="option" onclick="openWin()">도서 대출</a>
                		<a class="option" href="${path}/book/listCheckOutForm.do">대출 현황</a>
                		<a class="option" href="${path}/book/analyzeCheckOutForm.do">대출 분석</a>
            		</div>
            	</c:if>
            	<div id="mainnav2" class="mainnav active">도서</div>
            	<div class="subnav">
                	<a class="option">국내도서</a>
                	<a class="option">해외도서</a>
                	<a class="option">전자도서</a>
           		</div>
            	<div id="mainnav3" class="mainnav active">커뮤니티</div>
            	<div class="subnav">
                	<a class="option" href="${path}/board/boardForm.do?BOARD=free_board">자유게시판</a>
                	<a class="option" href="${path}/board/boardForm.do?BOARD=info_board">정보게시판</a>
                	<c:if test="${sessionScope.CUSTOMER !=null}">
                		<a class="option" href="${path}/message/sendMessageForm.do">메세지 보내기</a>
                		<a class="option" href="${path}/message/receiveMessageForm.do">메세지 보관함</a>
                	</c:if>
            	</div>
            		<c:if test="${sessionScope.CUSTOMER !=null}">
            			<div id="mainnav4" class="mainnav active">내 정보</div>
            			<div class="subnav">
                			<a class="option">대출 현황</a>
                			<a class="option" href="${path}/customer/infoForm.do">정보 변경</a>
            			</div>
            		</c:if>
            	<div id="mainnav5" class="mainnav active">기타</div>
            	<div class="subnav">
					<a class="option" href="${path}/board/boardForm.do?BOARD=qna_board">문의게시판</a>
            		<a class="option">서비스정보</a>
            	</div>
        	</div>
        	<div id="controlpanel">
            	<div>
                	<div id="darkmodebutton" class="controlbutton">
                    	<p style="left: 30%; top: 50%; transform: translate(-30%,-50%); color:white; font-weight:normal;">다크모드</p>
                    	<div class="buttons" style="left: 80%; top: 50%; transform: translate(-80%, -50%);">
                        	<div class="on"></div>
                        	<div class="off"></div>
                        	<div class="switch"></div>
                    	</div>
                	</div>
            	</div>
        	</div>
		</div>
    </div>
</body>
</html>