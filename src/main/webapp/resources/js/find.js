//============================================================================================
//사용자 아이디, 비밀번호찾기를 수행할 수 있도록 함.
//============================================================================================
$(document).ready(function(){
	
	
	
	
	
	//============================================================================================
	//사용자 ID찾기 클릭시, 사용자 ID찾기가 나타나고 사용자 PW 찾기는 사라짐.
	//사용자 PW찾기 클릭시, 사용자 PW찾기가 나타나고 사용자 ID 찾기는 사라짐.
	//============================================================================================
	$(document).on("click","#find_id, #find_pw",function(e){
		if($(this).attr("id")=="find_id"){
			$("#find_id").addClass("active");
			$("#find_pw").removeClass("active");
			$("#find_id_block").css({"display":"block"});
			$("#find_pw_block").css({"display":"none"});
		}else{
			$("#find_pw_select").addClass("active");
			$("#find_id_select").removeClass("active");
			$("#find_pw_block").css({"display":"block"});
			$("#find_id_block").css({"display":"none"});
		}
	});
	
	
	
	
	//============================================================================================
	//ID 또는 비밀번호 찾기 버튼 클릭시, 그에따라 서로다른 행동을 취함.
	//============================================================================================
	$(document).on("click","#find_by_phone, #find_by_email, #get_question_button, #find_password_button",function(){
		var flag = $(this).attr("id");
		var customer_id = $("#customer_id").val();
		var customer_phone = $("#customer_phone").val();
		var customer_email = $("#customer_email").val();
		var password_hint_answer = $("#password_hint_answer").val();
		
		
		
		
		//============================================================================================
		//백엔드 서버로부터 RSA2048 공개키를 얻음.
		//클라이언트는 해당 공개키로 비밀번호 찾기 질문에 대한 답을 암호화한후에 백엔드 서버로 전송.
		//============================================================================================
    	$.ajax({
    		"url":"/LibraryService/customer/getPublicKey.do",
    		"dataType":"json",
    		"type":"post",
    		"success":function(result){
    			//백엔드 서버로부터 공개키를 얻음, 대응되는 개인키는 백엔드 서버에서 세션에 담아둠.
    			var publickey = result.publickey;
    			//해당 공개키로 비밀번호찾기질문의 답을 암호화함.
        		password_hint_answer = encryptByRSA2048(password_hint_answer,publickey);
    			$.ajax({
    				"url":"/LibraryService/customer/find.do",
    				"type":"post",
    				"dataType":"json",
    				"data":{
    					//flag는 전화번호로 ID찾기, 이메일로 ID찾기, 비밀번호 찾기 질문 얻기, 비밀번호 찾기중
    					//어떤 요청인지 구분하기 위한 변수임.
    					"flag":flag,
    					"customer_id":customer_id,
    					"customer_phone":customer_phone,
    					"customer_email":customer_email,
    					"password_hint_answer":password_hint_answer
    				},
    				"success":function(result){
    					//이미 로그인 된 상태라면 굳이 아이디, 비밀번호를 찾을 필요 없으므로 오류처리.
    					if(flag=="find_by_phone"){
    						alert(result.content);
    					}else if(flag=="find_by_email"){
    						alert(result.content);
    					}else if(flag=="get_question_button"){
    						$("#password_question_list_content").text(result.password_question);
    					}else{
    						//비밀번호 찾기 질문에 대한 답이 만약 맞았다면, 백엔드 서버에서 가입한 이메일로 임시 비밀번호를 전달.
    						//클라이언트는 해당 비밀번호로 로그인후 비밀번호를 변경해야함.
    						//비밀번호를 단방향 해시함수와 솔트값을 이용해 더블 해싱했으므로 복호화가 불가능함.
    						alert(result.content);
    					}
    				},
    				"error":function(xhr, status, error){
    	    			alert(JSON.parse(xhr.responseText).content);
    				}
    			});
    		},
    		"error":function(xhr, status, error){
    			alert(JSON.parse(xhr.responseText).content);
    		}
    	});
	});
	
})