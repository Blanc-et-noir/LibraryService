//CNT는 현재 첨부된 파일의 개수, MAX는 최대 첨부가능한 파일의 개수임.
var CNT,MAX=12;




//============================================================================================
//영어는 1바이트, 한글은 3바이트로 계산, 오라클 DBMS기준, 게시글 제목은 300, 게시글 내용은 3000바이트 이하여야함.
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




//============================================================================================
//게시글에 이미지 파일을 첨부하면, 해당 이미지 파일을 미리볼 수 있도록 함.
//============================================================================================
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
	//인덱스는 각 이미지 파일들을 백엔드 서버로 전송할때 이름이 중복되지 않도록 하기위한 변수, 매번 1씩 증가함.
	var index = 1;
	CNT = $("input[type='file']").length;
	
	
	
	
	//============================================================================================
	//게시글을 작성 버튼 클릭시 게시글 작성 요청을 수행.
	//============================================================================================
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
			//FormData 객체에 게시글 제목, 내용, 게시판 종류, 부모 게시글 번호, 이미지 파일들을 담고 전송함.
			var formData = new FormData();
			formData.append("ARTICLE_TITLE",$("#ARTICLE_TITLE").val());
			formData.append("ARTICLE_CONTENT",$("#ARTICLE_CONTENT").val());
			formData.append("BOARD",$("#BOARD").val());
			formData.append("PARENT_ARTICLE_ID",$("#PARENT_ARTICLE_ID").val());
			
			var length = $("input[name^='file']").length;
			for(var i=0; i<length;i++){
				formData.append("file"+i, $("input[name^='file']")[i].files[0]);
			}
			
			//비동기방식으로 게시글 작성요청.
			$.ajax({
				"url":"/LibraryService/board/addArticle.do",
				"type":"POST",
				"data":formData,
				processData: false,
				contentType: false,
				"success":function(result){
					//게시글 작성에 성공하면 게시판으로 되돌아감.
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
	
	
	
	
	//============================================================================================
	//목록보기 버튼 클릭시, 게시글 목록으로 되돌아가는 메소드.
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
	//파일 추가버튼 클릭하면 파일을 첨부할 수 있도록 div태그를 생성.
	//============================================================================================
	$(document).on("click","#FILE_ADD_BUTTON",function(){
		if(CNT<MAX){
			CNT++;
			var div = $("<div id='fileList"+index+"' class='fileList'></div>");
			div.append("<label class='label' for='file"+index+"'></label>");
			div.append("<input id='file"+index+"' class='file' name='file"+index+"'type='file'>");
			div.append("<img class='image' name='file"+index+"'/>");
			
			//게시글에 첨부한 파일이 변경되면 미리보기 이미지를 그에 맞게 수정함.
			$(document).on("change","#file"+index,preview);
			
			//게시글에 첨부한 이미지 파일을 삭제하는 버튼
			var remove = $("<a class='remove' id='remove"+index+"'>✖</a>");
			
			
			
			
			//============================================================================================
			//게시글 삭제버튼 클릭시 해당 div태그를 삭제하는 이벤트를 등록함.
			//============================================================================================
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