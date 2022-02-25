$(document).ready(function(){
	function check(str) {
		var regExp = /^[a-z0-9_]{8,16}$/;		
		if(!regExp.test(str)) {
			return false; 
		} else { 
			return true; 
		} 
	}
    $(document).on("click","#LOGIN_BUTTON",function(e){
    	var CUSTOMER_ID = $("#CUSTOMER_ID").val();
    	var CUSTOMER_PW = $("#CUSTOMER_PW").val();
        if(!check(CUSTOMER_ID)||!check(CUSTOMER_PW)){
        	alert("아이디와 비밀번호는 알파벳과 숫자로 8자리이상 16자리이하 입니다.");
        }else{
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"text",
        		"type":"POST",
        		"success":function(result){
        			var PUBLICKEY = result;
        			CUSTOMER_PW = encryptByRSA2048(CUSTOMER_PW,PUBLICKEY);
                	$.ajax({
                		"url":"/LibraryService/customer/login.do",
                		"type":"POST",
                		"data":{
                			"CUSTOMER_ID":CUSTOMER_ID,
                			"CUSTOMER_PW":CUSTOMER_PW
                		},
                		"success":function(result){
                			console.log(result);
                			if(result.FLAG=="FALSE"){
                				alert(result.CONTENT);
                			}else if(result.FLAG=="LOGON"){
                				alert(result.CONTENT);
                				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
                				$("body").append(form);
                				form.submit();
                			}else{
                				var form = $("<form method='post' action='/LibraryService/customer/mainForm.do'></form>");
                				$("body").append(form);
                				form.submit();
                			}
                		},
                		"error":function(a,b,c){
                			alert("에러");
                		}
                	})
        		},
        		"error":function(){
        			alert("에러");
        		}
        	});
        }
    });
})