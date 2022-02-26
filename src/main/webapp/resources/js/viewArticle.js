var CNT,MAX=12,INDEX=100000000;




//============================================================================================
//게시글 수정시에 첨부된 이미지 파일 삭제 클릭시 수행할 메소드.
//============================================================================================
function removeFile(target){
	$(target).parent().off();
	$(target).parent().remove();
	CNT--;
}





//============================================================================================
//게시글에 이미지 파일을 첨부하면 해당 이미지를 미리 볼 수 있도록 하는 메소드.
//============================================================================================
function preview(input){
	var id = $(input).attr("id");
	if(input.files&&input.files[0]){
		var reader = new FileReader();
		reader.onload = function(e){
					
			$("img[name='"+id+"']").attr({
				"src":e.target.result
			});
		}
		reader.readAsDataURL(input.files[0]);
	}
}





//============================================================================================
//해당 이미지가 기존이미지와 달라졌을때, 원본 이미지 파일의 임시 이름을 삭제함으로써
//백엔드 서버에서 게시글 수정 처리시에 활용할 수 있음.
//============================================================================================
function changeFile(target){
	$(target).siblings("input[type='hidden']").remove();
}




//============================================================================================
//해당 문자열이 특정 바이트 이하의 크기를 갖는지 확인하는 메소드.
//게시글 제목은 300, 게시글 내용은 3000바이트 이하여야 하며
//오라클 DBMS기준 영어는 1바이트, 한글은 3바이트 크기를 차지함.
//============================================================================================
function checkBytes(text, MAX_BYTES){
	var sum = 0;
	for(var i=0;i<text.length;i++){
		if(escape(text.charAt(i)).length>4){
			sum=sum+3;
		}else{
			sum++;
		}
	}
	if(sum>MAX_BYTES){
		return false;
	}else{
		return true;
	}
}





$(document).ready(function(){
	CNT = $("input[type='file']").length;
	var BOARD = $("#BOARD").val();
	
	
	
	
	
	//============================================================================================
	//게시글 수정버튼 클릭시 버튼들을 적절히 활성화, 비활성화함.
	//============================================================================================
	$(document).on("click","#revise", function(){
		$("#control1").css({
			"display":"none"
		});
		$("#control2").css({
			"display":"flex"
		});
		$("#FILE_ADD_BUTTON").css({
			"display":"block"
		});
		$("input[type='file']").prop({
			"disabled":false
		})
		$(".v").attr({
			"disabled":false
		})
	})
	
	
	
	
	
	//============================================================================================
	//게시글 수정 취소 버튼 클릭시 버튼들을 적절히 활성화, 비활성화함.
	//============================================================================================
	$(document).on("click","#cancel", function(){
		$("#control2").css({
			"display":"none"
		});
		$("#control1").css({
			"display":"flex"
		});
		$("#FILE_ADD_BUTTON").css({
			"display":"none"
		});
		$(".v").attr({
			"disabled":true
		})
	})
	
	
	
	
	
	//============================================================================================
	//게시글 수정 확인 클릭시 백엔드 서버에 게시글 수정 요청을 수행하는 메소드.
	//============================================================================================
	$(document).on("click","#confirm",function(){
		var response = confirm("글을 수정합니다.");

		if($("#ARTICLE_TITLE").val().length == 0){
			alert("게시글 제목을 입력해야합니다.");
		}else if($("#ARTICLE_CONTENT").val().length == 0){
			alert("게시글 내용을 입력해야합니다.");
		}else if(!checkBytes($("#ARTICLE_TITLE").val(), 300)){
			alert("게시글 제목은 한글 100자, 영어 및 숫자 300자로 구성되어야 합니다.");
		}else if(!checkBytes($("#ARTICLE_CONTENT").val(), 3000)){
			alert("게시글 내용은 한글 1000자, 영어 및 숫자 3000자로 구성되어야 합니다.");
		}else{
			if(response==true){
				//게시글 수정시에 필요한 게시글 제목, 내용, 이미지 파일등은 FormDate 객체에 담아서 한번에 전송.
				var formData = new FormData();
				formData.append("ARTICLE_ID",$("#ARTICLE_ID").val());
				formData.append("ARTICLE_TITLE",$("#ARTICLE_TITLE").val());
				formData.append("ARTICLE_CONTENT",$("#ARTICLE_CONTENT").val());
				formData.append("BOARD",$("#BOARD").val());
				
				var str="";
				
				//수정되지 않은 이미지 파일들의 임시 이름들도 같이 전송함.
				var len=$("input[name='FILE_TEMP_NAME']").length;

				$.grep( $("input[name='FILE_TEMP_NAME']"), function ( value, index ){
					if(index<len-1){
						str+=$(value).val()+"*";
					}else{
						str+=$(value).val();
					}
				});
				
				formData.append("FILE_TEMP_NAMES",str);
				
				var length = $("input[name^='file']").length;
				for(var i=0; i<length;i++){
					formData.append("file"+i, $("input[name^='file']")[i].files[0]);
				}
				
				//============================================================================================
				//게시글 수정을 비동기식으로 요청함. 게시글 수정 성공시 게시판으로 이동함.
				//============================================================================================
				$.ajax({
					"url":"/LibraryService/board/modifyArticle.do",
					"type":"POST",
					"data":formData,
					processData: false,
					contentType: false,
					"success":function(result){
						var form = $("<form action='/LibraryService/board/boardForm.do' method='post'></form>");
						form.append("<input type='text' name='BOARD' value='"+$("#BOARD").val()+"'/>");
						$("body").append(form);
						form.submit();
					},
					"error":function(){
						alert("에러");
					}
				})
			}else{
				
			}
		}
	})
	
	
	
	
	
	//============================================================================================
	//게시글을 삭제하는 요청을 수행하는 메소드.
	//============================================================================================
	$(document).on("click","#remove",function(){
		var response = confirm("글을 삭제합니다.");
		if(response==true){
			var form = $("<form></form>");
			form.attr({
				"action":"/LibraryService/board/deleteArticle.do",
				"method":"post"
			});
			var input = $("<input/>");
			input.attr({
				"name":"ARTICLE_ID",
				"value":$("#ARTICLE_ID").attr("value")
			});
			form.append(input);
			var input2 = $("<input/>"); 
			input2.attr({
				"name":"BOARD",
				"value":BOARD
			});
			form.append(input2);
			$("body").append(form);
			form.submit();
		}
	})
	
	
	
	
	
	//============================================================================================
	//게시판으로 되돌아가는 요청을 수행하는 메소드.
	//============================================================================================
	$(document).on("click","#return",function(){
		var form = $("<form></form>");		
		form.attr({
			"action":"/LibraryService/board/boardForm.do",
			"method":"post"
		});
		form.append("<input name='BOARD' value='"+$("#BOARD").val()+"'/>");
		$("body").append(form);
		form.submit();
	})
	
	
	
	
	
	
	//============================================================================================
	//특정 게시글에 대해, 계층형으로 답변 게시글을 작성하는 화면을 요청하는 메소드.
	//============================================================================================
	$(document).on("click","#reply",function(){
		var form = $("<form></form>");		
		form.attr({
			"action":"/LibraryService/board/addArticleForm.do",
			"method":"post"
		});
		
		//부모게시글의 ID는 결국 현재 보고있는 게시글의 ID가 됨.
		form.append("<input name='PARENT_ARTICLE_ID' value='"+$("#ARTICLE_ID").val()+"'/>");
		form.append("<input name='BOARD' value='"+$("#BOARD").val()+"'/>");
		$("body").append(form);
		form.submit();
	})
	
	
	
	
	
	//============================================================================================
	//파일 추가버튼 클릭하면 파일을 첨부할 수 있도록 div태그를 생성.
	//============================================================================================
	$(document).on("click","#FILE_ADD_BUTTON",function(){
		if(CNT<MAX){
			var div = $("<div class='fileList'></div>");
			div.append("<img name='file"+INDEX+"'class='image' src='/LibraryService/resources/image/file_upload.svg'/>");
			div.append("<input style='display:none;' id='file"+(INDEX)+"' class='v' name='file"+INDEX+"' type='file' onchange='changeFile(this); preview(this)'>");	
			div.append("<label class='label' for='file"+INDEX+"'></label>");
			div.append("<input type='button' value='✖' class='v remove' onclick='removeFile(this);'>");
			$("#FILE").append(div);
			INDEX++;
			CNT++;
		}else{
			alert("최대 "+MAX+"개의 파일을 업로드할 수 있습니다.");
		}
	})
});