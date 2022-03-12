$(document).ready(function(){
	
	
	
	
	//============================================================================================
	//해당 문자열이 알파벳과 숫자로만 8자리 이상 16자리 이하로 구성되어있는지 확인하는 메소드.
	//============================================================================================
	function check(str) {
		var regExp = /^[a-z0-9_]{8,16}$/;		
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}

	
	
	
	//============================================================================================
	//해당 문자열이 010-0000-0000과 같은 형태로 구성되어있는지 확인하는 메소드.
	//============================================================================================
	function checkPhone(str) {
		var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
	
	
	
	
	
	//============================================================================================
	//해당 문자열이 aaaaaa@aaaaaa.com과 같은 형태로 구성되어있는지 확인하는 메소드.
	//============================================================================================
	function checkEmail(str) {
		var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
	
	//오늘 날짜를 YYYY-MM-DD의 형식으로 계산후, 해당 날짜 이후로의 날짜를 선택하지 못하도록 datelimit으로 설정함.
	//예를들어, 생년월일은 당연히 오늘날짜 이후로 설정할 수 없어야 함.
    var today = new Date();
    var year = today.getFullYear();
    var month,date;
    if(today.getMonth()+1<10){
    	month = "0"+(today.getMonth()+1);
    }else{
    	month = today.getMonth()+1;
    }
    if(today.getDate()+1<10){
    	date = "0"+(today.getDate());
    }else{
    	date = today.getDate();
    }
    
    //오늘 날짜를 계산후, 해당 날짜 이후로는 날짜를 선택할 수 없도록 속성을 설정함.
    var datelimit = year+"-"+month+"-"+date;
    $("#customer_bdate").attr({
    	"max":datelimit
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 찾기 질문들을 정적으로 미리 설정하는 것이 아니라 동적으로 DB에서 얻어와 리스트에 추가함.
    //============================================================================================
    $.ajax({
    	"type":"post",
    	"url":"/LibraryService/customer/getPasswordQuestionList.do",
    	"dataType":"json",
    	"success":function(result){
    		var list = result.list;
    		for(var i=0; i<list.length; i++){
    			$("#password_question_list_id").append("<option class='passwordQuestionList' value='"+list[i].password_question_list_id+"'>"+list[i].password_question_list_content+"</option>");
    		}
    	},
		"error": function(xhr, status, error) {
			  var err = JSON.parse(xhr.responseText);
			  alert(err.content);
		}
    });
    
    
    
    
    
    //============================================================================================
    //이메일 인증코드 요청 버튼을 클릭하면 가입하려고 하는 이메일로 인증코드를 요청함. 
    //============================================================================================
    $(document).on("click","#email_authcode_button",function(){
    	$.ajax({
    		"url":"/LibraryService/mail/sendEmailAuthCode.do",
    		"dataType":"json",
    		"type":"post",
    		"data":{
    			"customer_email":$("#customer_email").val()
    		},
    		"success":function(result){
    			alert(result.content);
    		},
    		"error": function(xhr, status, error) {
  			  var err = JSON.parse(xhr.responseText);
  			  alert(err.content);
    		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //이메일 인증 버튼을 클릭하면 클라이언트가 자신의 이메일로 전달받은 인증코드가 유효한지 아닌지 검증함.
    //============================================================================================
    $(document).on("click","#email_authemail_button",function(){
    	$.ajax({
    		"url":"/LibraryService/customer/authenticateEmail.do",
    		"dataType":"json",
    		"type":"post",
    		"data":{
    			"email_authcode":$("#email_authcode").val()
    		},
    		"success":function(result){
    			alert(result.content);
    		},
    		"error": function(xhr, status, error) {
  			  var err = JSON.parse(xhr.responseText);
  			  alert(err.content);
    		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정하기 버튼을 클릭하면 비밀번호 수정이 가능하도록 함.
    //============================================================================================
    $(document).on("click","#revise_password_button",function(){
    	$("#infoPanel1").css("display","none");
    	$("#infoPanel2").css("display","flex");
    	$("#customer_pw").attr("disabled",false);
    	$("#customer_pw_old").attr("disabled",false);
    	$("#customer_pw_check").attr("disabled",false);
    	$("#password_question_list_id").attr("disabled",false);
    	$("#password_hint_answer").attr("disabled",false);
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정하기 취소 버튼을 클릭하면 비밀번호 수정이 불가능 하도록 함.
    //============================================================================================
    $(document).on("click","#cancel_password_button",function(){
    	$("#infoPanel2").css("display","none");
    	$("#infoPanel1").css("display","flex");
    	$("#customer_pw").attr("disabled",true);
    	$("#customer_pw_old").attr("disabled",true);
    	$("#customer_pw_check").attr("disabled",true);
    	$("#password_question_list_id").attr("disabled",true);
    	$("#password_hint_answer").attr("disabled",true);
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정 완료하기 버튼을 클릭하면 변경된 비밀번호로 갱신을 요청함.
    //============================================================================================
    $(document).on("click","#change_password_button",function(){    	
    	
    	var customer_id = $("#customer_id").val();
    	var customer_pw = $("#customer_pw").val();
    	var customer_pw_old = $("#customer_pw_old").val();
    	var customer_pw_check = $("#customer_pw_check").val();

		var password_question_list_id = $("#password_question_list_id").val();
		var password_hint_answer = $("#password_hint_answer").val();
    
		if(!check(customer_pw)){
    		alert("비밀번호는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
    	}else if(customer_pw != customer_pw_check){
			alert("비밀번호가 서로 일치하지 않습니다.");
    	}else if(password_hint_answer.length == 0){
    		alert("비밀번호 찾기 질문에 대한 답은 공백일 수 없습니다.");
    	}else{
    		
    		
    		
    		
    		//============================================================================================
    		//백엔드 서버에게 RSA2048 공개키를 요청함. 공개키에 대응되는 비밀키는 백엔드 서버의 세션에 저장됨.
    		//============================================================================================
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"json",
        		"type":"post",
        		"success":function(result){
        			//전달받은 공개키로 사용자가 입력한 비밀번호와 비밀번호 찾기 질문에 대한 답을 암호화함.
        			var publickey = result.publickey;
        			customer_pw_old = encryptByRSA2048(customer_pw_old,publickey);
        			customer_pw = encryptByRSA2048(customer_pw,publickey);
        			password_hint_answer = encryptByRSA2048(password_hint_answer,publickey);
            		$.ajax({
            			"type":"post",
            			"url":"/LibraryService/customer/changePassword.do",
            			"dataType":"json",
            			"data":{
            				"customer_id":customer_id,
            				"customer_pw":customer_pw,
            				"customer_pw_old":customer_pw_old,
            				"password_question_list_id":password_question_list_id,
            				"password_hint_answer":password_hint_answer
            			},
            			"success":function(result){
        			    	$("#infoPanel2").css("display","none");
        			    	$("#infoPanel1").css("display","flex");
        			    	$("#customer_pw").attr("disabled",true);
        			    	$("#customer_pw_old").attr("disabled",true);
        			    	$("#customer_pw_check").attr("disabled",true);
        			    	$("#password_question_list_id").attr("disabled",true);
        			    	$("#password_hint_answer").attr("disabled",true);
        					alert(result.content);
        					
            				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
            				$("body").append(form);
            				form.submit();
            			},
                		"error": function(xhr, status, error) {
              			  var err = JSON.parse(xhr.responseText);
              			  alert(err.content);
                		}
            		});
        		},
        		"error": function(xhr, status, error) {
      			  var err = JSON.parse(xhr.responseText);
      			  alert(err.content);
        		}
        	});
        }
    });
    
    

    
    
    
    //============================================================================================
    //
    //============================================================================================
    $(document).on("click","#revise_other_button",function(){
    	$("#otherPanel1").css("display","none");
    	$("#otherPanel2").css("display","flex");
    	$("#customer_name").attr("disabled",false);
    	$("#customer_bdate").attr("disabled",false);
    	$("#customer_phone").attr("disabled",false);
    	$("#customer_email").attr("disabled",false);
    	$("#customer_address").attr("disabled",false);
    });
    
    
    
    
    
    //============================================================================================
    //기타 정보 변경버튼을 클릭하면 정보를 변경할 수 있도록 활성화함.
    //============================================================================================
    $(document).on("click","#cancel_other_button",function(){
    	$("#otherPanel2").css("display","none");
    	$("#otherPanel1").css("display","flex");
    	$("#customer_name").attr("disabled",true);
    	$("#customer_bdate").attr("disabled",true);
    	$("#customer_phone").attr("disabled",true);
    	$("#customer_email").attr("disabled",true);
    	$("#customer_address").attr("disabled",true);
    });    
    
    
    
    
    
    //============================================================================================
    //기타정보 변경 확인 버튼을 클릭하면 해당 정보로 변경을 요청함.
    //이때 이메일 인증 또한 다시 해야함.
    //============================================================================================
    $(document).on("click","#change_other_button",function(){   	
    	var customer_name = $("#customer_name").val();
    	var customer_bdate = $("#customer_bdate").val();
    	var customer_phone = $("#customer_phone").val();
    	var customer_email = $("#customer_email").val();
    	var customer_address = $("#customer_address").val();
    	if(customer_name.length == 0){
    		alert("이름은 공백일 수 없습니다.");
    	}else if(!checkPhone(customer_phone)){
    		alert("전화번호 형식이 잘못되었습니다.");
    	}else if(!checkEmail(customer_email)){
    		alert("이메일 형식이 잘못되었습니다.");
    	}else if(customer_address.length == 0){
    		alert("주소는 공백일 수 없습니다.");
    	}else{
    		$.ajax({
    			"type":"post",
    			"url":"/LibraryService/customer/changeOther.do",
    			"dataType":"json",
    			"data":{
    				"customer_name":customer_name,
    				"customer_bdate":customer_bdate,
    				"customer_phone":customer_phone,
    				"customer_email":customer_email,
    				"customer_address":customer_address
    			},
    			"success":function(result){
			    	$("#otherPanel2").css("display","none");
			    	$("#otherPanel1").css("display","flex");
			    	$("#customer_name").attr("disabled",true);
			    	$("#customer_bdate").attr("disabled",true);
			    	$("#customer_phone").attr("disabled",true);
			    	$("#customer_email").attr("disabled",true);
			    	$("#customer_address").attr("disabled",true);
					alert(result.content);
					
    				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
    				$("body").append(form);
    				form.submit();
    			},
        		"error": function(xhr, status, error) {
      			  var err = JSON.parse(xhr.responseText);
      			  alert(err.content);
        		}
    		});
        }
    });
})