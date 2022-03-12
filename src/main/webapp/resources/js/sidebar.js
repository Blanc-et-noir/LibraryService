//도서 대출 메뉴가 활성화 되었는지 여부를 나타냄.
var open = false;




//============================================================================================
//도서 대출 메뉴를 닫는 메소드.
//============================================================================================
function closeWin(){
	if(open==true){
		open = false;
		$("#checkOutForm").remove();
	}
}





//============================================================================================
//도서 대출 메뉴를 활성화 하는 메소드
//============================================================================================
function openWin(){  
	if(open==false){
		open = true;
		$("body").append("<form id='checkOutForm'><a id='checkOutFormCloseButton' onclick='closeWin()'>✖</a><input id='check_out_customer_id' type='text' name='customer_id' autocomplete='off' placeholder='사용자 ID' class='input2'><input id='check_out_book_isbn' type='text' name='book_isbn' autocomplete='off' placeholder='ISBN 코드' class='input2'><div style='display: flex; flex-direction: row; width: 100%; flex: 1;'><input id='check_out_button' type='button' value='대출하기'><input id='check_out_reset_button' type='reset' value='새로고침'></div></form>");
		//JQueryUI를 활용해 드래그가 가능하도록 설정.
		$("#checkOutForm").draggable({ containment: "parent", scroll: false });
	}
}  

$(document).ready(function(){
	
	
	
	
	
	//============================================================================================
	//도서 대출 메뉴의 대출하기 버튼 클릭시, 도서 대출 요청을 백엔드 서버에 전달.
	//파라미터로 사용자 ID와 도서 ISBN 코드가 필요.
	//============================================================================================
	$(document).on("click","#check_out_button",function(){
		if($("#check_out_customer_id").val().length == 0){
			alert("사용자 ID를 입력해야합니다.");
		}else if($("#check_out_book_isbn").val().length == 0){
			alert("ISBN 코드를 입력해야합니다.");
		}else{
			$.ajax({
				"url":"/LibraryService/book/checkOut.do",
				"type":"post",
				"data":{
					"customer_id":$("#check_out_customer_id").val(),
					"book_isbn":$("#check_out_book_isbn").val()
				},
				"success":function(result){
					alert(result.content);
					$("#check_out_book_isbn").val("");
				},
				"error":function(xhr, status, error){
	    			alert(JSON.parse(xhr.responseText).content);
				}
			})
		}
	});
	
	//쿠키를 활용해 내비게이션 메뉴의 서브메뉴들을 활성화 또는 비활성화 상태를 유지하도록 함.
	for(var i=1; i<=5; i++){
		if($.cookie("mainnav"+i)==undefined){
            $("#mainnav"+i).addClass("active");
            $("#mainnav"+i).next(".subnav").slideDown(300,"easeInOutExpo");
		}else{
            $("#mainnav"+i).removeClass("active");
            $("#mainnav"+i).next(".subnav").slideUp(300,"easeInOutExpo");
		}
	}
	
	//쿠키를 이용한 다크모드가 설정되어있을경우 다크모드를 설정하는 버튼을 우측으로 이동시키고, 다크모드 테마를 설정. 
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
	
	
	
	
	
	//============================================================================================
	//다크모드버튼 클릭시 다크모드 버튼이 움직이고, 다크모드 쿠키를 그에 맞게 추가 또는 제거함.
	//============================================================================================
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
    
    
    
    
    //============================================================================================
    //사이드바 활성화 버튼 클릭시, 사이드바 메뉴가 우측으로 슬라이드 되면서 포커싱됨.
    //============================================================================================
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
    
    
    
    
    
    //============================================================================================
    //메인메뉴 클릭시 하위 서브메뉴들이 드롭다운되면서, 쿠키를 추가 또는 제거함.
    //해당 쿠키를 이용해서 열림 또는 닫힘상태를 유지함.
    //============================================================================================
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