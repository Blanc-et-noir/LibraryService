//============================================================================================
//백엔드 서버로부터 메세지의 세부내용을 전달받고, 이 내용을 표시함.
//============================================================================================
$(document).ready(function(){
	
	
	
	//============================================================================================
	//되돌아가기 버튼 클릭시, 메세지 목록화면으로 이동함.
	//============================================================================================
	$(document).on("click","#MESSAGE_RETURN_BUTTON",function(){
		var form = $("<form action='/LibraryService/message/receiveMessageForm.do' method='post'/>");
		$("body").append(form);
		form.submit();
	})
	
	
	
	
	//============================================================================================
	//메세지 삭제 버튼 클릭시, 해당 메세지를 삭제하는 요청을 수행함.
	//============================================================================================
	$(document).on("click","#MESSAGE_DELETE_BUTTON",function(){
		deleteMessage();
	})
	
	
	
	
	//============================================================================================
	//메세지를 삭제하도록 요청하는 메소드.
	//============================================================================================
	function deleteMessage(){
		var MESSAGE_ID=$("#MESSAGE_ID").val();
		//송신함인지 수신함인지 구분하는 변수.
		var MESSAGE_BOX=$("#MESSAGE_BOX").val();
		var i;
		$.ajax({
			"url":"/LibraryService/message/deleteMessage.do",
			"dataType":"JSON",
			"type":"POST",
			"data":{
				"MESSAGE_BOX":MESSAGE_BOX,
				"MESSAGE_ID":MESSAGE_ID
			},
			"success":function(result){
				//메세지 삭제 성공시 메세지함으로 다시 이동함.
				if(result.FLAG=="TRUE"){
					alert(result.CONTENT);
					var form = $("<form action='/LibraryService/message/receiveMessageForm.do' method='post'/>");
					$("body").append(form);
					form.submit();
				}else{
					//메세지 삭제 실패시 오류가 있는 것이므로 메인화면으로 이동함.
					alert(result.CONTENT);
					var form = $("<form action='/LibraryService/message/mainForm.do' method='post'/>");
					$("body").append(form);
					form.submit();
				}
			},
			"error":function(){
				alert("에러");
			}
		});
	}
})