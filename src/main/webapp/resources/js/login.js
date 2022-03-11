//============================================================================================
//로그인을 위한 자바스크립트 코드.
//============================================================================================
$(document).ready(function(){
	
	
	
	
	//============================================================================================
	//정규식을 통해 8이상 16이상의 자릿수, 알파벳과 숫자로만 구성되어있는지 확인함.
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
	//로그인버튼 클릭시, 비밀번호는 암호화하여 백엔드서버로 전달해야함.
	//먼저 형식에 맞는지 확인후, 맞다면 서버로부터 공개키를 전달받음.
	//============================================================================================
    $(document).on("click","#login_button",function(e){
    	var customer_id = $("#customer_id").val();
    	var customer_pw = $("#customer_pw").val();
        if(!check(customer_id)||!check(customer_pw)){
        	alert("아이디와 비밀번호는 알파벳과 숫자로 8자리이상 16자리이하 입니다.");
        }else{	
        	//============================================================================================
        	//서버로 부터 공개키를 전달받는데, 이에 대응되는 개인키는 서버의 세션에 저장되어있음.
        	//============================================================================================
        	$.ajax({
        		"url":"/LibraryService/customer/getPublicKey.do",
        		"dataType":"json",
        		"type":"post",
        		"success":function(result){
        			//백엔드 서버로부터 공개키를 전달받음.
        			var publickey = result.publickey;
        			//전달받은 공개키로 RSA2048 암호화를 수행.
        			customer_pw = encryptByRSA2048(customer_pw,publickey);
                	$.ajax({
                		"url":"/LibraryService/customer/login.do",
                		"type":"post",
                		"dataType":"json",
                		"data":{
                			"customer_id":customer_id,
                			"customer_pw":customer_pw
                		},
                		"success":function(result){
                			if(result.flag=="true"){
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
                	})
        		},
        		"error": function(xhr, status, error) {
        			  var err = JSON.parse(xhr.responseText);
        			  alert(err.content);
          		}
        	});
        }
    });
})