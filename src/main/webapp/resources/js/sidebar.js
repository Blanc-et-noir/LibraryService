var open = false;

function closeWin(){
	if(open==true){
		open = false;
		$("#checkOutForm").remove();
	}
}
function openWin(){
	//window.open("/LibraryService/book/checkOutForm.do", "도서 대출", "width=350, height=220, toolbar=no, menubar=no, scrollbars=no, resizable=no" );  
	if(open==false){
		open = true;
		$("body").append("<form id='checkOutForm'><a id='checkOutFormCloseButton' onclick='closeWin()'>✖</a><input id='CHECK_OUT_CUSTOMER_ID' type='text' name='CUSTOMER_ID' autocomplete='off' placeholder='사용자 ID' class='input2'><input id='CHECK_OUT_BOOK_ISBN' type='text' name='BOOK_ISBN' autocomplete='off' placeholder='ISBN 코드' class='input2'><div style='display: flex; flex-direction: row; width: 100%; flex: 1;'><input id='CHECK_OUT_BUTTON' type='button' value='대출하기'><input id='CHECK_OUT_RESET_BUTTON' type='reset' value='새로고침'></div></form>");
		$("#checkOutForm").draggable({ containment: "parent", scroll: false });
	}
}  

$(document).ready(function(){
	
	$(document).on("click","#CHECK_OUT_BUTTON",function(){
		if($("#CHECK_OUT_CUSTOMER_ID").val().length == 0){
			alert("사용자 ID를 입력해야합니다.");
		}else if($("#CHECK_OUT_BOOK_ISBN").val().length == 0){
			alert("ISBN 코드를 입력해야합니다.");
		}else{
			$.ajax({
				"url":"/LibraryService/book/checkOut.do",
				"type":"POST",
				"data":{
					"CUSTOMER_ID":$("#CHECK_OUT_CUSTOMER_ID").val(),
					"BOOK_ISBN":$("#CHECK_OUT_BOOK_ISBN").val()
				},
				"success":function(result){
					alert(result.CONTENT);
					$("#CHECK_OUT_BOOK_ISBN").val("");
				},
				"error":function(){
					alert("에러");
				}
			})
		}
	});


	for(var i=1; i<=5; i++){
		if($.cookie("mainnav"+i)==undefined){
            $("#mainnav"+i).addClass("active");
            $("#mainnav"+i).next(".subnav").slideDown(300,"easeInOutExpo");
		}else{
            $("#mainnav"+i).removeClass("active");
            $("#mainnav"+i).next(".subnav").slideUp(300,"easeInOutExpo");
		}
	}
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
	
    $(document).on("click","#darkmodebutton",function(e){
        if(!$(this).find(".switch").is(":animated")){
            if($(this).hasClass("active")){
                $(this).removeClass("active");
                $(this).find(".switch").animate({
                    "left":0,
                    "right":"50%"
                },600,"easeInOutExpo",function(){
                    document.documentElement.setAttribute('color-theme', 'light');
                    $.removeCookie('darkmode',{path:'/'});                
                });
            }else{
                $(this).addClass("active");
                $(this).find(".switch").animate({
                    "right":0,
                    "left":"50%"
                },600,"easeInOutExpo",function(){
                    document.documentElement.setAttribute('color-theme', 'dark');
                    $.cookie('darkmode', 'true', {path:'/' });
                });  
            }
        }
    });
    
    $(document).on("click","#sidebarbutton",function(e){
        if(!$("#sidebar").is(":animated")){
            if($(this).hasClass("active")){
            	$("#sidebarbutton").text("☰");
                $(this).removeClass("active");
            	$("#cover").animate({
                    "opacity":"0"
                },600,"easeInOutExpo",function(){

                });
            	
                $("#sidebar").animate({
                    "left":"-270px"
                },600,"easeInOutExpo",function(){
                	$("#cover").css({
                		"display":"none"
                	});
                	
                });
            }else{
                $(this).addClass("active");
                $("#sidebarbutton").text("✖");
            	$("#cover").css({
            		"display":"block"
            	})
            	$("#cover").animate({
                    "opacity":"0.5"
                },600,"easeInOutExpo",function(){

                });
                $("#sidebar").animate({
                    "left":"0%"
                },600,"easeInOutExpo",function(){
                	
                });
            }
        }
    });

    $(document).on("click",".mainnav",function(e){
        if($(this).hasClass("active")){
            $(this).removeClass("active");
            $(this).next(".subnav").slideUp(300,"easeInOutExpo");
            $.cookie($(this).attr("id"), 'true', {path:'/' });
        }else{
            $(this).addClass("active");
            $(this).next(".subnav").slideDown(300,"easeInOutExpo");
            $.removeCookie($(this).attr("id"),{path:'/'});
        }
    });
})