//============================================================================================
//체크아웃을 더이상 독립적인 뷰에서 진행하지 않으므로 사용하지않는 코드.
//해당 코드는 sidebar.js에서 재사용함.
//============================================================================================
$(document).ready(function(){
	if($.cookie("darkmode")!=undefined){
		document.documentElement.setAttribute("color-theme", "dark");
		$("#darkmodebutton").addClass("active");
		$("#darkmodebutton .switch").animate({
            "right":0,
            "left":"50%"
		},0,"linear",function(){

		});
	}else{
		document.documentElement.setAttribute("color-theme", "light");
	}
	$(document).on("click","#CHECK_OUT_BUTTON",function(){
		if($("#CUSTOMER_ID").val().length == 0){
			alert("사용자 ID를 입력해야합니다.");
		}else if($("#BOOK_ISBN").val().length == 0){
			alert("ISBN 코드를 입력해야합니다.");
		}else{
			$.ajax({
				"url":"/LibraryService/book/checkOut.do",
				"type":"POST",
				"data":{
					"CUSTOMER_ID":$("#CUSTOMER_ID").val(),
					"BOOK_ISBN":$("#BOOK_ISBN").val()
				},
				"success":function(result){
					alert(result.CONTENT);
					$("#BOOK_ISBN").val("");
				},
				"error":function(){
					alert("에러");
				}
			})
		}
	});
});