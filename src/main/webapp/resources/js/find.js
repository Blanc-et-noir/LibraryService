$(document).ready(function(){
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
	$(document).on("click","#FIND_BY_PHONE, #FIND_BY_EMAIL, #GET_QUESTION_BUTTON, #FIND_PASSWORD_BUTTON",function(){
		var flag = $(this).attr("id");
		
		var CUSTOMER_ID = $("#CUSTOMER_ID").val();
		var CUSTOMER_PHONE = $("#CUSTOMER_PHONE").val();
		var CUSTOMER_EMAIL = $("#CUSTOMER_EMAIL").val();
		var PASSWORD_HINT_ANSWER = ($("#PASSWORD_HINT_ANSWER").val()).replaceAll(" ","");
		
    	$.ajax({
    		"url":"/LibraryService/customer/getPublicKey.do",
    		"dataType":"text",
    		"type":"POST",
    		"success":function(result){
    			var PUBLICKEY = result;
        		PASSWORD_HINT_ANSWER = encryptByRSA2048(PASSWORD_HINT_ANSWER,PUBLICKEY);
    			$.ajax({
    				"url":"/LibraryService/customer/find.do",
    				"type":"POST",
    				"dataType":"JSON",
    				"data":{
    					"flag":flag,
    					"CUSTOMER_ID":CUSTOMER_ID,
    					"CUSTOMER_PHONE":CUSTOMER_PHONE,
    					"CUSTOMER_EMAIL":CUSTOMER_EMAIL,
    					"PASSWORD_HINT_ANSWER":PASSWORD_HINT_ANSWER
    				},
    				"success":function(result){
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