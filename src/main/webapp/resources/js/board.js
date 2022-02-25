//============================================================================================
//백엔드로부터 응답받은 날짜데이터가 오늘을 기점으로 오늘인지, 과거인지 분류하는 메소드.
//최근이라면 true, 아니라면 false를 리턴.
//이는 최근날짜라면 시간만 00:00:00 시간으로, 최근이 아니라면 0000.00.00과 같이 날짜로 표기하고
//new 키워드를 붙일지 말지를 결정하기 위함.
//============================================================================================
function isRecent(ARTICLE_DATE_STRING){
	var temp1 = ARTICLE_DATE_STRING.split(" ");
	var temp2 = temp1[0].split(".");
	var TODAY = new Date();
	var Y = TODAY.getFullYear(), M = TODAY.getMonth(), D = TODAY.getDate();
	var t1 = new Date(temp2[0],temp2[1]-1,temp2[2]), t2 = new Date(Y,M,D);
	
	if(t1>=t2){
		return true;
	}else{
		return false;
	}
}



$(document).ready(function(){
    //SECTION, PAGE는 페이징을 위한 변수, BOARD는 게시판의 종류, SEARCH는 검색할 내용
	var BOARD = $("#listTable").attr("value"),SEARCH="", SECTION = 1, PAGE = 1;
	
	//페이지가 로딩됨과동시에 바로 게시글목록을 얻어온다.
	getArticles(SECTION,PAGE);
	
	//게시글 쓰기 버튼에 이벤트를 추가함, 게시글 쓰기 화면을 요청함.
	$(document).on("click","#write",function(){
		var form = $("<form></form>");
		form.attr({
			"action":"/LibraryService/board/addArticleForm.do",
			"method":"post"
		})
		//게시글을 새로 작성하는 것이므로 부모게시글은 없음. 따라서 부모 게시글번호는 0으로 고정.
		form.append("<input name='BOARD' value='"+BOARD+"'/>");
		form.append("<input name='PARENT_ARTICLE_ID' value='0'/>");
		$("body").append(form);
		form.submit();
	});	
	
	//검색버튼에 이벤트를 추가.
	$(document).on("click","#SEARCH_BUTTON",function(){
		SECTION=1;
		PAGE=1;
		SEARCH=$("#SEARCH").val();
		getArticles(SECTION,PAGE);
	});
	
	
	
	
	
	
	//============================================================================================
	//게시판 페이징을 수행하는 메소드.
	//백엔드로부터 조건에 해당하는 게시글들의 총 개수인 TOTAL값을 받으면 자동으로 페이징을 진행함.
	//============================================================================================
	function paging(TOTAL){
		var maxSECTION = Math.ceil(TOTAL/100);
		var maxPAGE = Math.ceil((TOTAL-(SECTION-1)*100)/10)>=10?10:Math.ceil((TOTAL-(SECTION-1)*100)/10);

		$("#paging").off();
		
		$("#paging").empty();
		
		var tag;
		
		//현재 섹션이 2이상이면 즉, 101번째 이상의 게시글을 조회하는 것이면, 이전 100개의 게시글을 조회할 수 있는 이전 버튼을 추가함.
		if(SECTION>=2){
			tag = $("<a id='prev'>이전</a>");
			$("#paging").on("click","#prev",function(){
				SECTION--;
				PAGE=1;
				getArticles(SECTION,PAGE);
			})
			$("#paging").append(tag);
		}
		
		//해당 섹션에 존재할 수 있는 페이지 번호수에 따라 페이지를 동적으로 추가함.
		for(var i=1; i<=maxPAGE;i++){
			$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
			$("#paging").on("click","#idx"+i,function(){
				getArticles(SECTION,$(this).attr("value"));
				PAGE = $(this).attr("value");
			})
		}
		
		$("#paging a").removeClass("active");
		$("#idx"+PAGE).addClass("active");
		
		//현재 섹션이 아직 섹션의 최대값이 아닌경우, 아직 게시글들을 더 불러올 수 있으므로 다음 버튼을 추가함.
		if(SECTION<maxSECTION){
			tag = $("<a id='next'>다음</a>");
			$("#paging").on("click","#next",function(){
				SECTION++;
				PAGE=1;
				getArticles(SECTION,PAGE);
			})
			$("#paging").append(tag);
		}
	}
	
	
	
	
	
	//============================================================================================
	//게시글들의 목록을 얻어오는 메소드, 파라미터로 섹션과 페이지값을 필요로함.
	//============================================================================================
	function getArticles(SECTION,PAGE){
		$.ajax({
			"url":"/LibraryService/board/listArticles.do",
			"type":"post",
			"dataType":"json",
			"data":{
				"BOARD":BOARD,
				"SECTION":SECTION,
				"PAGE":PAGE,
				"SEARCH":SEARCH,
				//사용자 ID, 제목, 내용중 어떤 것을 기준으로 게시글을 검색할지 나타내는 플래그변수.
				"FLAG":$("#FLAG").val()
			},
			"success":function(result){
				var TOTAL = result.TOTAL;
				//해당 검색조건에 맞는 게시글의 개수가 0이거나, 게시글읽기에 실패한경우 그에 대응되는 처리를 진행.
				if(result.FLAG == "FALSE"||TOTAL==0){
					SECTION=1;
					PAGE=1;
					$("#listTable").empty();
					var tr = $("<tr></tr>");
					tr.append("<th>번호</th>");
					tr.append("<th>작성자</th>");
					tr.append("<th>제목</th>");
					tr.append("<th>작성일</th>");
					tr.append("<th>조회</th>");
					$("#listTable").append(tr);
					tr = $("<tr></tr>");
					tr.append("<td colspan=5>게시글 정보가 존재하지 않습니다.</td>");
					$("#listTable").append(tr);
					paging(0);
				}else{
					//게시글 목록이 1개이상 존재하고 조회에 성공했을때, 게시글들을 나열함.
					$("#listTable").empty();
					var tr = $("<tr></tr>");
					tr.append("<th>번호</th>");
					tr.append("<th>작성자</th>");
					tr.append("<th>제목</th>");
					tr.append("<th>작성일</th>");
					tr.append("<th>조회</th>");
					$("#listTable").append(tr);
					
					//얻은 게시글들의 개수는 최대 10개임.
					for(var i=0; i<result.LIST.length;i++){
						var recent = isRecent(result.LIST[i].ARTICLE_DATE_STRING);
						var NEW = recent==true?" <span class='new'>[new]</span>":"";
						var temp = result.LIST[i].ARTICLE_DATE_STRING.split(" ");
						
						tr = $("<tr></tr>");
						tr.append("<td>"+(i+1)+"</td>");
						tr.append("<td>"+result.LIST[i].CUSTOMER_ID+"</td>");
						
						//최상위 게시글이 아니라면, 해당 게시글에 화살표를 표시함으로써 부모 게시글에 대한 자식 게시글임을 나타냄.
						if(result.LIST[i].LVL>1){
							tr.append("<td style='text-indent:"+((result.LIST[i].LVL-1)*20)+"px;'><a href='/LibraryService/board/viewArticle.do?BOARD="+BOARD+"&ARTICLE_ID="+result.LIST[i].ARTICLE_ID+"'> ➥   "+result.LIST[i].ARTICLE_TITLE+NEW+"</a></td>");
						}else{
							//최상위 게시글이라면, 화살표를 사용하지 않음.
							tr.append("<td style='text-indent:"+((result.LIST[i].LVL-1)*20)+"px;'><a href='/LibraryService/board/viewArticle.do?BOARD="+BOARD+"&ARTICLE_ID="+result.LIST[i].ARTICLE_ID+"'>"+result.LIST[i].ARTICLE_TITLE+NEW+"</a></td>");
						}
						
						//해당 게시글이 비교적 최신의 게시글이면 시간을, 아니면 날짜를 표기함.
						if(recent){
							tr.append("<td>"+temp[1]+"</td>");
						}else{
							tr.append("<td>"+temp[0]+"</td>");
						}

						tr.append("<td>"+result.LIST[i].ARTICLE_VIEWS+"</td>");
						$("#listTable").append(tr);
					}
					
					//게시글을 모두 나열한후에 페이징을 진행함.
					paging(TOTAL);
				}
			},
			//게시글 읽기과정중 오류 발생시, 게시글 읽기 실패시의 처리와 동일하게 처리함.
			"error":function(a,b,c){
				alert("에러");
				SECTION=1;
				PAGE=1;
				$("#listTable").empty();
				var tr = $("<tr></tr>");
				tr.append("<th>번호</th>");
				tr.append("<th>작성자</th>");
				tr.append("<th>제목</th>");
				tr.append("<th>작성일</th>");
				tr.append("<th>조회</th>");
				$("#listTable").append(tr);
				tr = $("<tr></tr>");
				tr.append("<td colspan=5>게시글 정보가 존재하지 않습니다.</td>");
				$("#listTable").append(tr);
				paging(0);
			}
		})
	}
});