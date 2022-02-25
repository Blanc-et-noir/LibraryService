//============================================================================================
//사용자 아이디, 비밀번호찾기를 수행할 수 있도록 함.
//============================================================================================
$(document).ready(function(){
	
	
	
	
	
	//============================================================================================
	//사용자 ID찾기 클릭시, 사용자 ID찾기가 나타나고 사용자 PW 찾기는 사라짐.
	//사용자 PW찾기 클릭시, 사용자 PW찾기가 나타나고 사용자 ID 찾기는 사라짐.
	//============================================================================================
	$(document).on("click","#FIND_ID, #FIND_PW",function(e){
		if($(this).attr("id")=="FIND_ID"){
			$("#FIND_ID").addClass("active");
			$("#FIND_PW").removeClass("active");
			$("#FIND_ID_BLOCK").css({"display":"block"});
			$("#FIND_PW_BLOCK").css({"display":"none"});
		}else{
			$("#FIND_PW_SELECT").addClass("active");
			$("#FIND_ID_SELECT").removeClass("active");
			$("#FIND_PW_BLOCK").css({"display":"block"});
			$("#FIND_ID_BLOCK").css({"display":"none"});
		}
	});
	
	
	
	
	//============================================================================================
	//ID 또는 비밀번호 찾기 버튼 클릭시, 그에따라 서로다른 행동을 취함.
	//============================================================================================
	$(document).on("click","#FIND_BY_PHONE, #FIND_BY_EMAIL, #GET_QUESTION_BUTTON, #FIND_PASSWORD_BUTTON",function(){
		var flag = $(this).attr("id");
		
		var CUSTOMER_ID = $("#CUSTOMER_ID").val();
		var CUSTOMER_PHONE = $("#CUSTOMER_PHONE").val();
		var CUSTOMER_EMAIL = $("#CUSTOMER_EMAIL").val();
		var PASSWORD_HINT_ANSWER = ($("#PASSWORD_HINT_ANSWER").val()).replaceAll(" ","");
		
		
		
		
		//============================================================================================
		//백엔드 서버로부터 RSA2048 공개키를 얻음.
		//클라이언트는 해당 공개키로 비밀번호 찾기 질문에 대한 답을 암호화한후에 백엔드 서버로 전송.
		//============================================================================================
    	$.ajax({
    		"url":"/LibraryService/customer/getPublicKey.do",
    		"dataType":"text",
    		"type":"POST",
    		"success":function(result){
    			//백엔드 서버로부터 공개키를 얻음, 대응되는 개인키는 백엔드 서버에서 세션에 담아둠.
    			var PUBLICKEY = result;
    			//해당 공개키로 비밀번호찾기질문의 답을 암호화함.
        		PASSWORD_HINT_ANSWER = encryptByRSA2048(PASSWORD_HINT_ANSWER,PUBLICKEY);
    			$.ajax({
    				"url":"/LibraryService/customer/find.do",
    				"type":"POST",
    				"dataType":"JSON",
    				"data":{
    					//flag는 전화번호로 ID찾기, 이메일로 ID찾기, 비밀번호 찾기 질문 얻기, 비밀번호 찾기중
    					//어떤 요청인지 구분하기 위한 변수임.
    					"flag":flag,
    					"CUSTOMER_ID":CUSTOMER_ID,
    					"CUSTOMER_PHONE":CUSTOMER_PHONE,
    					"CUSTOMER_EMAIL":CUSTOMER_EMAIL,
    					"PASSWORD_HINT_ANSWER":PASSWORD_HINT_ANSWER
    				},
    				"success":function(result){
    					//이미 로그인 된 상태라면 굳이 아이디, 비밀번호를 찾을 필요 없으므로 오류처리.
    					if(result.FLAG=="LOGON"){
    	    				alert(result.CONTENT);
            				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
            				$("body").append(form);
            				form.submit();
    					}else if(flag=="FIND_BY_PHONE"){
    						alert(result.CONTENT);
    					}else if(flag=="FIND_BY_EMAIL"){
    						alert(result.CONTENT);
    					}else if(flag=="GET_QUESTION_BUTTON"){
    						$("#PASSWORD_QUESTION_LIST_CONTENT").text(result.CONTENT);
    					}else{
    						//비밀번호 찾기 질문에 대한 답이 만약 맞았다면, 백엔드 서버에서 가입한 이메일로 임시 비밀번호를 전달.
    						//클라이언트는 해당 비밀번호로 로그인후 비밀번호를 변경해야함.
    						//비밀번호를 단방향 해시함수와 솔트값을 이용해 더블 해싱했으므로 복호화가 불가능함.
    						alert(result.CONTENT);
    					}
    				},
    				"error":function(){
    	    			console.log("에러");
    				}
    			});
    		},
    		"error":function(){
    			console.log("에러");
    		}
    	});
	});
	
})