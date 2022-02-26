$(document).ready(function(){
	
	
	
	
	//============================================================================================
	//해당 문자열이 일정 바이트 이하인지 아닌지를 검증하는 메소드.
	//영어는 1바이트, 한글은 3바이트로 계산함, 오라클 DBMS 기준.
	//============================================================================================
	function checkBytes(text, MAX_BYTES){
		var sum = 0;
		for(var i=0;i<text.length;i++){
			if(escape(text.charAt(i)).length>4){
				sum=sum+3;
			}else{
				sum++;
			}
		}
		if(sum>MAX_BYTES){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	
	
	//============================================================================================
	//메세지 송신버튼을 클릭하면, 백엔드 서버에 메세지 송신을 요청하는 메소드.
	//============================================================================================
	$(document).on("click","#MESSAGE_SEND_BUTTON",function(){
		var RECEIVER_ID = $("#CUSTOMER_ID").val();
		var MESSAGE_TITLE = $("#MESSAGE_TITLE").val();
		var MESSAGE_CONTENT = $("#MESSAGE_CONTENT").val();
		if(RECEIVER_ID.length == 0){
			alert("메세지를 받을 사람의 아이디를 입력해야합니다.");
		}else if(MESSAGE_TITLE.length == 0){
			alert("메세지 제목을 입력해야합니다.");
		}else if(MESSAGE_CONTENT.length == 0){
			alert("메세지 내용을 입력해야합니다.");
		}else if(!checkBytes(MESSAGE_TITLE,300)){
			alert("메세지 제목은 한글 100자, 영어 및 숫자 300자로 구성되어야 합니다.");
		}else if(!checkBytes(MESSAGE_CONTENT,3000)){
			alert("메세지 내용은 한글 1000자, 영어 및 숫자 3000자로 구성되어야 합니다.");
		}else{
			
			
			
			
			//============================================================================================
			//메세지 송신을 비동기식으로 백엔드 서버에 요청함.
			//============================================================================================
			$.ajax({
				"url":"/LibraryService/message/sendMessage.do",
				"type":"POST",
				"dataType":"JSON",
				"data":{
					"RECEIVER_ID":RECEIVER_ID,
					"MESSAGE_TITLE":MESSAGE_TITLE,
					"MESSAGE_CONTENT":MESSAGE_CONTENT
				},
				"success":function(result){
					console.log(result);
					if(result.FLAG =="TRUE"){
						alert(result.CONTENT);
						$("#CUSTOMER_ID").val("");
						$("#MESSAGE_TITLE").val("");
						$("#MESSAGE_CONTENT").val("");
					}else{
						alert(result.CONTENT);
					}
				},
				"error":function(){
					alert("에러");
				}
			})
		}
	});
})