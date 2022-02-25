var CNT,MAX=12;
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

function preview(){
	var id = $(this).attr("id");
	if(this.files&&this.files[0]){
		var reader = new FileReader();
		reader.onload = function(e){
					
			$("img[name='"+id+"']").attr({
				"src":e.target.result
			});
		}
		reader.readAsDataURL(this.files[0]);
	}
}

$(document).ready(function(){
	var index = 1;
	CNT = $("input[type='file']").length;
	$(document).on("click","#addArticle",function(){
		if($("#ARTICLE_TITLE").val().length == 0){
			alert("게시글 제목을 입력해야합니다.");
		}else if($("#ARTICLE_CONTENT").val().length == 0){
			alert("게시글 내용을 입력해야합니다.");
		}else if(!checkBytes($("#ARTICLE_TITLE").val(), 300)){
			alert("게시글 제목은 한글 100자, 영어 및 숫자 300자로 구성되어야 합니다.");
		}else if(!checkBytes($("#ARTICLE_CONTENT").val(), 3000)){
			alert("게시글 내용은 한글 1000자, 영어 및 숫자 3000자로 구성되어야 합니다.");
		}else{
			var formData = new FormData();
			formData.append("ARTICLE_TITLE",$("#ARTICLE_TITLE").val());
			formData.append("ARTICLE_CONTENT",$("#ARTICLE_CONTENT").val());
			formData.append("BOARD",$("#BOARD").val());
			formData.append("PARENT_ARTICLE_ID",$("#PARENT_ARTICLE_ID").val());
			
			var length = $("input[name^='file']").length;
			for(var i=0; i<length;i++){
				formData.append("file"+i, $("input[name^='file']")[i].files[0]);
			}
			
			$.ajax({
				"url":"/LibraryService/board/addArticle.do",
				"type":"POST",
				"data":formData,
				processData: false,
				contentType: false,
				"success":function(result){
					alert(result.CONTENT);
					var form = $("<form action='/LibraryService/board/boardForm.do' method='post'></form>");
					form.append("<input type='text' name='BOARD' value='"+$("#BOARD").val()+"'/>");
					$("body").append(form);
					form.submit();
				},
				"error":function(){
					alert("에러");
				}
			})
		}
	});
	
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
	
	$(document).on("click","#FILE_ADD_BUTTON",function(){
		if(CNT<MAX){
			CNT++;
			var div = $("<div id='fileList"+index+"' class='fileList'></div>");
			div.append("<label class='label' for='file"+index+"'></label>");
			div.append("<input id='file"+index+"' class='file' name='file"+index+"'type='file'>");
			div.append("<img class='image' name='file"+index+"'/>");
			
			$(document).on("change","#file"+index,preview);
			
			var remove = $("<a class='remove' id='remove"+index+"'>✖</a>");
			$(document).on("click","#remove"+index,function(){
				$(this).parent().off();
				$(this).parent().remove();
				cnt--;
			});
			div.append(remove);
			index++;
			$("#FILE").append(div);
		}else{
			alert("최대 "+MAX+"개의 파일을 업로드할 수 있습니다.");
		}
	})
});