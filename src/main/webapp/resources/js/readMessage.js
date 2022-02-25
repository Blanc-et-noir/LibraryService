$(document).ready(function(){
	
	$(document).on("click","#MESSAGE_RETURN_BUTTON",function(){
		var form = $("<form action='/LibraryService/message/receiveMessageForm.do' method='post'/>");
		$("body").append(form);
		form.submit();
	})
	
	$(document).on("click","#MESSAGE_DELETE_BUTTON",function(){
		deleteMessage();
	})
	
	function deleteMessage(){
		var MESSAGE_ID=$("#MESSAGE_ID").val();
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
				if(result.FLAG=="TRUE"){
					alert(result.CONTENT);
					var form = $("<form action='/LibraryService/message/receiveMessageForm.do' method='post'/>");
					$("body").append(form);
					form.submit();
				}else{
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