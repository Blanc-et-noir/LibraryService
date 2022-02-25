var CNT,MAX=12,INDEX=100000;
function removeFile(target){
	$(target).parent().off();
	$(target).parent().remove();
	CNT--;
}
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

function changeFile(target){
	$(target).siblings("input[type='hidden']").remove();
}
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
				var formData = new FormData();
				formData.append("ARTICLE_ID",$("#ARTICLE_ID").val());
				formData.append("ARTICLE_TITLE",$("#ARTICLE_TITLE").val());
				formData.append("ARTICLE_CONTENT",$("#ARTICLE_CONTENT").val());
				formData.append("BOARD",$("#BOARD").val());
				
				var str="";
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
		}else{
			
		}
	})
	
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
	
	$(document).on("click","#reply",function(){
		var form = $("<form></form>");		
		form.attr({
			"action":"/LibraryService/board/addArticleForm.do",
			"method":"post"
		});
		form.append("<input name='PARENT_ARTICLE_ID' value='"+$("#ARTICLE_ID").val()+"'/>");
		form.append("<input name='BOARD' value='"+$("#BOARD").val()+"'/>");
		$("body").append(form);
		form.submit();
	})
	
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