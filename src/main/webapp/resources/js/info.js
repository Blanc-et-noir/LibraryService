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
    $("#CUSTOMER_BDATE").attr({
    	"max":datelimit
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 찾기 질문들을 정적으로 미리 설정하는 것이 아니라 동적으로 DB에서 얻어와 리스트에 추가함.
    //============================================================================================
    $.ajax({
    	"type":"POST",
    	"url":"/LibraryService/customer/getPasswordQuestionList.do",
    	"dataType":"json",
    	"success":function(arr){
    		for(var i=0; i<arr.length; i++){
    			$("#PASSWORD_QUESTION_LIST_ID").append("<option class='passwordQuestionList' value='"+arr[i].PASSWORD_QUESTION_LIST_ID+"'>"+arr[i].PASSWORD_QUESTION_LIST_CONTENT+"</option>");
    		}
    	},
    	"error":function(){
    		alert("error");
    	}
    });
    
    
    
    
    
    //============================================================================================
    //이메일 인증코드 요청 버튼을 클릭하면 가입하려고 하는 이메일로 인증코드를 요청함. 
    //============================================================================================
    $(document).on("click","#EMAIL_AUTHCODE_BUTTON",function(){
    	$.ajax({
    		"url":"/LibraryService/mail/sendEmailAuthCode.do",
    		"dataType":"JSON",
    		"type":"POST",
    		"data":{
    			"CUSTOMER_EMAIL":$("#CUSTOMER_EMAIL").val()
    		},
    		"success":function(result){
    			if(result.FLAG == "LOGON"){
    				alert(result.CONTENT);
    			}else{
    				alert(result.CONTENT);
    			}
    		},
    		"error":function(){
    			alert("에러");
    		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //이메일 인증 버튼을 클릭하면 클라이언트가 자신의 이메일로 전달받은 인증코드가 유효한지 아닌지 검증함.
    //============================================================================================
    $(document).on("click","#EMAIL_AUTHEMAIL_BUTTON",function(){
    	$.ajax({
    		"url":"/LibraryService/customer/authenticateEmail.do",
    		"dataType":"JSON",
    		"type":"POST",
    		"data":{
    			"EMAIL_AUTHCODE":$("#EMAIL_AUTHCODE").val()
    		},
    		"success":function(result){
    			if(result.FLAG == "LOGON"){
    				alert(result.CONTENT);
    			}else{
    				alert(result.CONTENT);
    			}
    		},
    		"error":function(){
    			alert("에러");
    		}
    	})
    })
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정하기 버튼을 클릭하면 비밀번호 수정이 가능하도록 함.
    //============================================================================================
    $(document).on("click","#REVISE_PASSWORD_BUTTON",function(){
    	$("#infoPanel1").css("display","none");
    	$("#infoPanel2").css("display","flex");
    	$("#CUSTOMER_PW").attr("disabled",false);
    	$("#CUSTOMER_PW_OLD").attr("disabled",false);
    	$("#CUSTOMER_PW_CHECK").attr("disabled",false);
    	$("#PASSWORD_QUESTION_LIST_ID").attr("disabled",false);
    	$("#PASSWORD_HINT_ANSWER").attr("disabled",false);
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정하기 취소 버튼을 클릭하면 비밀번호 수정이 불가능 하도록 함.
    //============================================================================================
    $(document).on("click","#CANCEL_PASSWORD_BUTTON",function(){
    	$("#infoPanel2").css("display","none");
    	$("#infoPanel1").css("display","flex");
    	$("#CUSTOMER_PW").attr("disabled",true);
    	$("#CUSTOMER_PW_OLD").attr("disabled",true);
    	$("#CUSTOMER_PW_CHECK").attr("disabled",true);
    	$("#PASSWORD_QUESTION_LIST_ID").attr("disabled",true);
    	$("#PASSWORD_HINT_ANSWER").attr("disabled",true);
    });
    
    
    
    
    
    //============================================================================================
    //비밀번호 수정 완료하기 버튼을 클릭하면 변경된 비밀번호로 갱신을 요청함.
    //============================================================================================
    $(document).on("click","#CHANGE_PASSWORD_BUTTON",function(){    	
    	
    	var CUSTOMER_ID = $("#CUSTOMER_ID").val();
    	var CUSTOMER_PW = $("#CUSTOMER_PW").val();
    	var CUSTOMER_PW_OLD = $("#CUSTOMER_PW_OLD").val();
    	var CUSTOMER_PW_CHECK = $("#CUSTOMER_PW_CHECK").val();

		var PASSWORD_QUESTION_LIST_ID = $("#PASSWORD_QUESTION_LIST_ID").val();
		var PASSWORD_HINT_ANSWER = $("#PASSWORD_HINT_ANSWER").val();
    
		if(!check(CUSTOMER_PW)){
    		alert("비밀번호는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
    	}else if(CUSTOMER_PW != CUSTOMER_PW_CHECK){
			alert("비밀번호가 서로 일치하지 않습니다.");
    	}else if(PASSWORD_HINT_ANSWER.length == 0){
    		alert("비밀번호 찾기 질문에 대한 답은 공백일 수 없습니다.");
    	}else{
    		
    		
    		
    		
    		//============================================================================================
    		//백엔드 서버에게 RSA2048 공개키를 요청함. 공개키에 대응되는 비밀키는 백엔드 서버의 세션에 저장됨.
    		//============================================================================================
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"text",
        		"type":"POST",
        		"success":function(result){
        			//전달받은 공개키로 사용자가 입력한 비밀번호와 비밀번호 찾기 질문에 대한 답을 암호화함.
        			var PUBLICKEY = result;
        			CUSTOMER_PW_OLD = encryptByRSA2048(CUSTOMER_PW_OLD,PUBLICKEY);
            		CUSTOMER_PW = encryptByRSA2048(CUSTOMER_PW,PUBLICKEY);
            		PASSWORD_HINT_ANSWER = encryptByRSA2048(PASSWORD_HINT_ANSWER,PUBLICKEY);
            		$.ajax({
            			"type":"POST",
            			"url":"/LibraryService/customer/changePassword.do",
            			"dataType":"JSON",
            			"data":{
            				"CUSTOMER_ID":CUSTOMER_ID,
            				"CUSTOMER_PW":CUSTOMER_PW,
            				"CUSTOMER_PW_OLD":CUSTOMER_PW_OLD,
            				"PASSWORD_QUESTION_LIST_ID":PASSWORD_QUESTION_LIST_ID,
            				"PASSWORD_HINT_ANSWER":PASSWORD_HINT_ANSWER
            			},
            			"success":function(result){
            				if(result.FLAG=="TRUE"){
            			    	$("#infoPanel2").css("display","none");
            			    	$("#infoPanel1").css("display","flex");
            			    	$("#CUSTOMER_PW").attr("disabled",true);
            			    	$("#CUSTOMER_PW_OLD").attr("disabled",true);
            			    	$("#CUSTOMER_PW_CHECK").attr("disabled",true);
            			    	$("#PASSWORD_QUESTION_LIST_ID").attr("disabled",true);
            			    	$("#PASSWORD_HINT_ANSWER").attr("disabled",true);
            					alert(result.CONTENT);
            					
            				//로그아웃 상태일 경우에는 정보를 변경할 수 없음.
            				}else if(result.FLAG=="LOGOFF"){
                				alert(result.CONTENT);
                				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
                				$("body").append(form);
                				form.submit();
            				}else{
            					alert(result.CONTENT);
            				}
            			},
            			"error":function(){
            				alert("에러");
            			}
            		});
        		},
        		"error":function(){
        			console.log("에러");
        		}
        	});
        }
    });
    
    

    
    
    
    //============================================================================================
    //
    //============================================================================================
    $(document).on("click","#REVISE_OTHER_BUTTON",function(){
    	$("#otherPanel1").css("display","none");
    	$("#otherPanel2").css("display","flex");
    	$("#CUSTOMER_NAME").attr("disabled",false);
    	$("#CUSTOMER_BDATE").attr("disabled",false);
    	$("#CUSTOMER_PHONE").attr("disabled",false);
    	$("#CUSTOMER_EMAIL").attr("disabled",false);
    	$("#CUSTOMER_ADDRESS").attr("disabled",false);
    });
    
    
    
    
    
    //============================================================================================
    //기타 정보 변경버튼을 클릭하면 정보를 변경할 수 있도록 활성화함.
    //============================================================================================
    $(document).on("click","#CANCEL_OTHER_BUTTON",function(){
    	$("#otherPanel2").css("display","none");
    	$("#otherPanel1").css("display","flex");
    	$("#CUSTOMER_NAME").attr("disabled",true);
    	$("#CUSTOMER_BDATE").attr("disabled",true);
    	$("#CUSTOMER_PHONE").attr("disabled",true);
    	$("#CUSTOMER_EMAIL").attr("disabled",true);
    	$("#CUSTOMER_ADDRESS").attr("disabled",true);
    });    
    
    
    
    
    
    //============================================================================================
    //기타정보 변경 확인 버튼을 클릭하면 해당 정보로 변경을 요청함.
    //이때 이메일 인증 또한 다시 해야함.
    //============================================================================================
    $(document).on("click","#CHANGE_OTHER_BUTTON",function(){   	
    	var CUSTOMER_NAME = $("#CUSTOMER_NAME").val();
    	var CUSTOMER_BDATE = $("#CUSTOMER_BDATE").val();
    	var CUSTOMER_PHONE = $("#CUSTOMER_PHONE").val();
    	var CUSTOMER_EMAIL = $("#CUSTOMER_EMAIL").val();
    	var CUSTOMER_ADDRESS = $("#CUSTOMER_ADDRESS").val();
    	if(CUSTOMER_NAME.length == 0){
    		alert("이름은 공백일 수 없습니다.");
    	}else if(!checkPhone(CUSTOMER_PHONE)){
    		alert("전화번호 형식이 잘못되었습니다.");
    	}else if(!checkEmail(CUSTOMER_EMAIL)){
    		alert("이메일 형식이 잘못되었습니다.");
    	}else if(CUSTOMER_ADDRESS.length == 0){
    		alert("주소는 공백일 수 없습니다.");
    	}else{
    		$.ajax({
    			"type":"POST",
    			"url":"/LibraryService/customer/changeOther.do",
    			"dataType":"JSON",
    			"data":{
    				"CUSTOMER_NAME":CUSTOMER_NAME,
    				"CUSTOMER_BDATE":CUSTOMER_BDATE,
    				"CUSTOMER_PHONE":CUSTOMER_PHONE,
    				"CUSTOMER_EMAIL":CUSTOMER_EMAIL,
    				"CUSTOMER_ADDRESS":CUSTOMER_ADDRESS
    			},
    			"success":function(result){
    				if(result.FLAG=="TRUE"){
    			    	$("#otherPanel2").css("display","none");
    			    	$("#otherPanel1").css("display","flex");
    			    	$("#CUSTOMER_NAME").attr("disabled",true);
    			    	$("#CUSTOMER_BDATE").attr("disabled",true);
    			    	$("#CUSTOMER_PHONE").attr("disabled",true);
    			    	$("#CUSTOMER_EMAIL").attr("disabled",true);
    			    	$("#CUSTOMER_ADDRESS").attr("disabled",true);
    					alert(result.CONTENT);
    				}else if(result.FLAG=="LOGOFF"){
        				alert(result.CONTENT);
        				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
        				$("body").append(form);
        				form.submit();
    				}else{
    					alert(result.CONTENT);
    				}
    			},
    			"error":function(){
    				alert("에러");
    			}
    		});
        }
    });
})