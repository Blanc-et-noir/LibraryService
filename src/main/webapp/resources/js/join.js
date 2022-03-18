$(document).ready(function(){
	//현재 날짜 이후로는 날짜를 선택할 수 없도록 함.
    var today = new Date();
    var year = today.getFullYear();
    var month,date;
    if(today.getMonth()+1<10){
    	month = "0"+(today.getMonth()+1);
    }else{
    	month = today.getMonth()+1;
    }
    if(today.getDate()<10){
    	date = "0"+(today.getDate());
    }else{
    	date = today.getDate();
    }
    var datelimit = year+"-"+month+"-"+date;
    $("#customer_bdate").attr({
    	"max":datelimit
    });
    
    
    
    
    
    //============================================================================================
    //정적 방식이 아니라 비동기 동적 방식으로 비밀번호 찾기 질문들을 리스트에 추가함.
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
			openPopup(JSON.parse(xhr.responseText).content);
  		}
    });
    
    
    
    
    
    
    //============================================================================================
    //이메일 인증코드 발급을 요청하는 메소드, 백엔드 서버에서 클라이언트가 입력한 해당 이메일로 이메일 인증 번호를 전달함.
    //============================================================================================
    $(document).on("click","#email_authcode_button",function(){
    	var customer_email = $("#customer_email").val();
    	if(!checkEmail(customer_email)){
    		openPopup("이메일 형식이 잘못되었습니다.");
    		return;
    	}
    	$.ajax({
    		"url":"/LibraryService/mail/sendEmailAuthCode.do",
    		"dataType":"json",
    		"type":"post",
    		"data":{
    			"customer_email":$("#customer_email").val()
    		},
    		"success":function(result){
    			openPopup(result.content);
    		},
    		"error": function(xhr, status, error) {
    			openPopup(JSON.parse(xhr.responseText).content);
      		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //클라이언트가 전달받은 인증코드를 제대로 입력했는지, 해당 이메일이 정말 존재하는지 인증코드를 통해 이메일을 인증하는 메소드.
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
    			openPopup(result.content);
    		},
    		"error": function(xhr, status, error) {
    			openPopup(JSON.parse(xhr.responseText).content);
    		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //회원가입 버튼을 클릭하면 회원가입을 백엔드 서버에 요청하는 메소드.
    //============================================================================================
    $(document).on("click","#join_button",function(){
    	var customer_id = $("#customer_id").val();
    	var customer_pw = $("#customer_pw").val();
    	var customer_pw_check = $("#customer_pw_check").val();
    	var customer_name = $("#customer_name").val();
    	var customer_phone = $("#customer_phone").val();
    	var customer_email = $("#customer_email").val();
    	var customer_address = $("#customer_address").val();
    	var customer_bdate = $("#customer_bdate").val();
		var password_question_list_id = $("#password_question_list_id").val();
		var password_hint_answer = $("#password_hint_answer").val();
    
		if(!check(customer_id)){
			openPopup("아이디는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
		}else if(!check(customer_pw)){
    		openPopup("비밀번호는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
    	}else if(customer_pw != customer_pw_check){
			openPopup("비밀번호가 서로 일치하지 않습니다.");
    	}else if(!checkPhone(customer_phone)){
    		openPopup("전화번호 형식이 잘못되었습니다.");
    	}else if(!checkEmail(customer_email)){
    		openPopup("이메일 형식이 잘못되었습니다.");
    	}else if(customer_address.length == 0){
			openPopup("주소는 공백일 수 없습니다.");
    	}else if(password_hint_answer.length == 0){
    		openPopup("비밀번호 찾기 질문에 대한 답은 공백일 수 없습니다.");
    	}else{
    		
    		
    		
    		
    		//============================================================================================
    		//백엔드 서버로부터 공개키를 요청하는 메소드. 이에 대응되는 비밀키는 백엔드 서버의 세션에 저장됨.
    		//============================================================================================
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"json",
        		"type":"post",
        		"success":function(result){
        			
        			//전달받은 공개키로 비밀번호와 비밀번호 찾기 질문에 대한 답을 암호화 함.
        			var publickey = result.publickey;
            		customer_pw = encryptByRSA2048(customer_pw,publickey);
            		password_hint_answer = encryptByRSA2048(password_hint_answer,publickey);
            		
            		
            		
            		
            		
            		//============================================================================================
            		//해당 사용자 정보로 회원가입을 요청하는 메소드.
            		//============================================================================================
            		$.ajax({
            			"type":"post",
            			"url":"/LibraryService/customer/join.do",
            			"dataType":"json",
            			"data":{
            				"customer_id":customer_id,
            				"customer_pw":customer_pw,
            				"customer_name":customer_name,
            				"customer_phone":customer_phone,
            				"customer_email":customer_email,
            				"customer_address":customer_address,
            				"customer_bdate":customer_bdate,
            				"password_question_list_id":password_question_list_id,
            				"password_hint_answer":password_hint_answer
            			},
            			"success":function(result){
            				if(result.flag=="true"){
            					openPopup(result.content);
                				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
                				$("body").append(form);
                				form.submit();
            				}else{
            					openPopup(result.content);
            				}
            			},
                		"error": function(xhr, status, error) {
                			openPopup(JSON.parse(xhr.responseText).content);
                		}
            		});
        		},
        		"error": function(xhr, status, error) {
        			openPopup(JSON.parse(xhr.responseText).content);
          		}
        	});
        }
    });    
})