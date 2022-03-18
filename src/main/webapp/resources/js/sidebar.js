//도서 대출 메뉴가 활성화 되었는지 여부를 나타냄.
var open = false;
var popupFlag = false;


function check(str) {
	var regExp = /^[a-z0-9_]{8,16}$/;		
	if(str.length==0||!regExp.test(str)) {
		return false; 
	} else { 
		return true; 
	} 
}

function checkISBN(str){
	var regExp = /[0-9]{13,13}$/;
	if(str.length!=13||!regExp.text(str)){
		return false;
	}else{
		return true;
	}
}




//============================================================================================
//해당 문자열이 010-0000-0000과 같이 전화번호의 형식으로 되어있는지 확인하는 메소드.
//============================================================================================
function checkPhone(str) {
	var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
	if(str.length==0||!regExp.test(str)) {
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
	if(str.length==0||!regExp.test(str)) {
		return false; 
	} else { 
		return true; 
	} 
}

function openPopup(str){
	if(popupFlag==false){
		popupFlag=true;
		var popup = "<div id='popup'><p id='popupMessage'>"+str+"</p><div id='popupButton' onclick='closePopup();'>확인</div></div>";
    	$("#popupCover").css({
    		"display":"block"
    	})
    	$("#popupCover").animate({
            "opacity":"0.5"
        },0,"easeInOutExpo",function(){
        	$("body").append(popup);
        });
    	
	}
}
function closePopup(){
	if(popupFlag==true){
		popupFlag=false;
		$("#popup").remove();
		
    	$("#popupCover").animate({
            "opacity":"0"
        },0,"easeInOutExpo",function(){
        	$("#popupCover").css({
        		"display":"none"
        	})
        });
    	
	}
}


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
		var check_out_book_isbn = $("#check_out_book_isbn").val();
		var check_out_customer_id = $("#check_out_customer_id").val();
		
		if(!check(check_out_customer_id)){
			openPopup("아이디는 알파벳과 숫자로 8자리이상 16자리이하로 구성해야 합니다.");
		}else if(!checkISBN(check_out_book_isbn)){
			openPopup("ISBN코드는 13자리의 숫자로 구성해야 합니다.");
		}else{
			$.ajax({
				"url":"/LibraryService/book/checkOut.do",
				"type":"post",
				"data":{
					"customer_id":$("#check_out_customer_id").val(),
					"book_isbn":$("#check_out_book_isbn").val()
				},
				"success":function(result){
					openPopup(result.content);
					$("#check_out_book_isbn").val("");
				},
				"error":function(xhr, status, error){
					openPopup(JSON.parse(xhr.responseText).content);
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