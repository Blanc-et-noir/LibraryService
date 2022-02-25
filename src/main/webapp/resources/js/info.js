$(document).ready(function(){
	function check(str) {
		var regExp = /^[a-z0-9_]{8,16}$/;		
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}

	function checkPhone(str) {
		var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
	
	function checkEmail(str) {
		var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
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
    
    var datelimit = year+"-"+month+"-"+date;

    $("#CUSTOMER_BDATE").attr({
    	"max":datelimit
    });
    
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
    $(document).on("click","#REVISE_PASSWORD_BUTTON",function(){
    	$("#infoPanel1").css("display","none");
    	$("#infoPanel2").css("display","flex");
    	$("#CUSTOMER_PW").attr("disabled",false);
    	$("#CUSTOMER_PW_OLD").attr("disabled",false);
    	$("#CUSTOMER_PW_CHECK").attr("disabled",false);
    	$("#PASSWORD_QUESTION_LIST_ID").attr("disabled",false);
    	$("#PASSWORD_HINT_ANSWER").attr("disabled",false);
    });
    $(document).on("click","#CANCEL_PASSWORD_BUTTON",function(){
    	$("#infoPanel2").css("display","none");
    	$("#infoPanel1").css("display","flex");
    	$("#CUSTOMER_PW").attr("disabled",true);
    	$("#CUSTOMER_PW_OLD").attr("disabled",true);
    	$("#CUSTOMER_PW_CHECK").attr("disabled",true);
    	$("#PASSWORD_QUESTION_LIST_ID").attr("disabled",true);
    	$("#PASSWORD_HINT_ANSWER").attr("disabled",true);
    });
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
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"text",
        		"type":"POST",
        		"success":function(result){
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
    
    

    
    
    
    
    $(document).on("click","#REVISE_OTHER_BUTTON",function(){
    	$("#otherPanel1").css("display","none");
    	$("#otherPanel2").css("display","flex");
    	$("#CUSTOMER_NAME").attr("disabled",false);
    	$("#CUSTOMER_BDATE").attr("disabled",false);
    	$("#CUSTOMER_PHONE").attr("disabled",false);
    	$("#CUSTOMER_EMAIL").attr("disabled",false);
    	$("#CUSTOMER_ADDRESS").attr("disabled",false);
    });
    $(document).on("click","#CANCEL_OTHER_BUTTON",function(){
    	$("#otherPanel2").css("display","none");
    	$("#otherPanel1").css("display","flex");
    	$("#CUSTOMER_NAME").attr("disabled",true);
    	$("#CUSTOMER_BDATE").attr("disabled",true);
    	$("#CUSTOMER_PHONE").attr("disabled",true);
    	$("#CUSTOMER_EMAIL").attr("disabled",true);
    	$("#CUSTOMER_ADDRESS").attr("disabled",true);
    });    
    
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