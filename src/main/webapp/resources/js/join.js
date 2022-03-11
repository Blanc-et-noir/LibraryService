$(document).ready(function(){
	//============================================================================================
	//해당 문자열이 알파벳과 숫자만으로 8자리 이상, 16자리 이하로 구성되어있는지 확인하는 메소드
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
	//해당 문자열이 010-0000-0000과 같이 전화번호의 형식으로 되어있는지 확인하는 메소드.
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
	//해당 문자열이 aaaaaa@aaaaaa.com과 같이 이메일의 형식으로 되어있는지 확인하는 메소드.
	//============================================================================================
	function checkEmail(str) {
		var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
	
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
    $("#CUSTOMER_BDATE").attr({
    	"max":datelimit
    });
    
    
    
    
    
    //============================================================================================
    //정적 방식이 아니라 비동기 동적 방식으로 비밀번호 찾기 질문들을 리스트에 추가함.
    //============================================================================================
    $.ajax({
    	"type":"POST",
    	"url":"/LibraryService/customer/getPasswordQuestionList.do",
    	"dataType":"json",
    	"success":function(result){
    		var list = result.list;
    		console.log(list);
    		for(var i=0; i<list.length; i++){
    			$("#PASSWORD_QUESTION_LIST_ID").append("<option class='passwordQuestionList' value='"+list[i].password_question_list_id+"'>"+list[i].password_question_list_content+"</option>");
    		}
    	},
		"error": function(xhr, status, error) {
			  var err = JSON.parse(xhr.responseText);
			  alert(err.content);
  		}
    });
    
    
    
    
    
    
    //============================================================================================
    //이메일 인증코드 발급을 요청하는 메소드, 백엔드 서버에서 클라이언트가 입력한 해당 이메일로 이메일 인증 번호를 전달함.
    //============================================================================================
    $(document).on("click","#EMAIL_AUTHCODE_BUTTON",function(){
    	$.ajax({
    		"url":"/LibraryService/mail/sendEmailAuthCode.do",
    		"dataType":"JSON",
    		"type":"POST",
    		"data":{
    			"customer_email":$("#CUSTOMER_EMAIL").val()
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
    //클라이언트가 전달받은 인증코드를 제대로 입력했는지, 해당 이메일이 정말 존재하는지 인증코드를 통해 이메일을 인증하는 메소드.
    //============================================================================================
    $(document).on("click","#EMAIL_AUTHEMAIL_BUTTON",function(){
    	$.ajax({
    		"url":"/LibraryService/customer/authenticateEmail.do",
    		"dataType":"JSON",
    		"type":"POST",
    		"data":{
    			"email_authcode":$("#EMAIL_AUTHCODE").val()
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
    //회원가입 버튼을 클릭하면 회원가입을 백엔드 서버에 요청하는 메소드.
    //============================================================================================
    $(document).on("click","#JOIN_BUTTON",function(){
    	var customer_id = $("#CUSTOMER_ID").val();
    	var customer_pw = $("#CUSTOMER_PW").val();
    	var customer_pw_check = $("#CUSTOMER_PW_CHECK").val();
    	var customer_name = $("#CUSTOMER_NAME").val();
    	var customer_phone = $("#CUSTOMER_PHONE").val();
    	var customer_email = $("#CUSTOMER_EMAIL").val();
    	var customer_address = $("#CUSTOMER_ADDRESS").val();
    	var customer_bdate = $("#CUSTOMER_BDATE").val();
    	
    	//KIND_NUMBER가 0이면 관리자, 1이면 고객임.
    	var kind_number = $("#KIND_NUMBER").val();
		var password_question_list_id = $("#PASSWORD_QUESTION_LIST_ID").val();
		var password_hint_answer = $("#PASSWORD_HINT_ANSWER").val();
    
		if(!check(customer_id)){
			alert("아이디는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
		}else if(!check(customer_pw)){
    		alert("비밀번호는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
    	}else if(customer_pw != customer_pw_check){
			alert("비밀번호가 서로 일치하지 않습니다.");
    	}else if(password_hint_answer.length == 0){
    		alert("비밀번호 찾기 질문에 대한 답은 공백일 수 없습니다.");
    	}else if(customer_name.length == 0){
    		alert("이름은 공백일 수 없습니다.");
    	}else if(!checkPhone(customer_phone)){
    		alert("전화번호 형식이 잘못되었습니다.");
    	}else if(!checkEmail(customer_email)){
    		alert("이메일 형식이 잘못되었습니다.");
    	}else if(customer_address.length == 0){
    		alert("주소는 공백일 수 없습니다.");
    	}else{
    		
    		
    		
    		
    		//============================================================================================
    		//백엔드 서버로부터 공개키를 요청하는 메소드. 이에 대응되는 비밀키는 백엔드 서버의 세션에 저장됨.
    		//============================================================================================
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"json",
        		"type":"POST",
        		"success":function(result){
        			
        			console.log(result);
        			
        			
        			//전달받은 공개키로 비밀번호와 비밀번호 찾기 질문에 대한 답을 암호화 함.
        			var publickey = result.publickey;
            		customer_pw = encryptByRSA2048(customer_pw,publickey);
            		password_hint_answer = encryptByRSA2048(password_hint_answer,publickey);
            		
            		
            		
            		
            		
            		//============================================================================================
            		//해당 사용자 정보로 회원가입을 요청하는 메소드.
            		//============================================================================================
            		$.ajax({
            			"type":"POST",
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
            				console.log(result);
            				if(result.flag=="true"){
            					alert(result.content);
                				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
                				$("body").append(form);
                				form.submit();
            				}else{
            					alert(result.content);
            				}
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
})