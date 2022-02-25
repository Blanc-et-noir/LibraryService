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

function paging(TOTAL){
	var maxSection = Math.ceil(TOTAL/100);
	var maxPage = Math.ceil((TOTAL-(section-1)*100)/10)>=10?10:Math.ceil((TOTAL-(section-1)*100)/10);
	
	$("#paging").off();
	
	$("#paging").empty();
	
	var tag;
	if(section>=2){
		tag = $("<a id='prev'>이전</a>");
		$("#paging").on("click","#prev",function(){
			section--;
			page=1;
			receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
		})
		$("#paging").append(tag);
	}
	for(var i=1; i<=maxPage;i++){
		$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
		
		$("#paging").on("click","#idx"+i,function(){
			receiveMessage(MESSAGE_BOX, ORDERBY, section, $(this).attr("value"))
			page = $(this).attr("value");
		})
	}
	$("#paging a").removeClass("active");
	$("#idx"+page).addClass("active");
	if(section<maxSection){
		tag = $("<a id='next'>다음</a>");
		$("#paging").on("click","#next",function(){
			section++;
			page=1;
			receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
		})
		$("#paging").append(tag);
	}
}

$(document).ready(function(){

    var today = new Date();
    var year = today.getFullYear();
    var month,date;
    if(today.getMonth()+1<10){
    	month = "0"+(today.getMonth()+1);
    }else{
    	month = today.getMonth()+1;
    }
    if(today.getDate()<10){
    	date = "0"+(today.getDate());
    }else{
    	date = today.getDate();
    }
    
    var datelimit = year+"-"+month+"-"+date;

    
    $("#CUSTOMER_BDATE").attr({
    	"max":datelimit
    });
	
	var BOARD = $("#listTable").attr("value"), SECTION = 1, PAGE = 1;
	var SEARCH="";
	
	getArticles(SECTION,PAGE);
	
	$(document).on("click","#write",function(){
		var form = $("<form></form>");
		form.attr({
			"action":"/LibraryService/board/addArticleForm.do",
			"method":"post"
		})

		form.append("<input name='BOARD' value='"+BOARD+"'/>");
		form.append("<input name='PARENT_ARTICLE_ID' value='0'/>");
		$("body").append(form);
		form.submit();
	});	
	
	$(document).on("click","#SEARCH_BUTTON",function(){
		SECTION=1;
		PAGE=1;
		SEARCH=$("#SEARCH").val();
		getArticles(SECTION,PAGE);
	});
	function paging(TOTAL){
		var maxSECTION = Math.ceil(TOTAL/100);
		var maxPAGE = Math.ceil((TOTAL-(SECTION-1)*100)/10)>=10?10:Math.ceil((TOTAL-(SECTION-1)*100)/10);

		$("#paging").off();
		
		$("#paging").empty();
		
		var tag;
		if(SECTION>=2){
			tag = $("<a id='prev'>이전</a>");
			$("#paging").on("click","#prev",function(){
				SECTION--;
				PAGE=1;
				getArticles(SECTION,PAGE);
			})
			$("#paging").append(tag);
		}
		for(var i=1; i<=maxPAGE;i++){
			$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
			
			$("#paging").on("click","#idx"+i,function(){
				getArticles(SECTION,$(this).attr("value"));
				PAGE = $(this).attr("value");
			})
		}
		$("#paging a").removeClass("active");
		$("#idx"+PAGE).addClass("active");
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
				"FLAG":$("#FLAG").val()
			},
			"success":function(result){
				var TOTAL = result.TOTAL;
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
					$("#listTable").empty();
					var tr = $("<tr></tr>");
					tr.append("<th>번호</th>");
					tr.append("<th>작성자</th>");
					tr.append("<th>제목</th>");
					tr.append("<th>작성일</th>");
					tr.append("<th>조회</th>");
					$("#listTable").append(tr);
					for(var i=0; i<result.LIST.length;i++){
						var recent = isRecent(result.LIST[i].ARTICLE_DATE_STRING);
						var NEW = recent==true?" <span class='new'>[new]</span>":"";
						var temp = result.LIST[i].ARTICLE_DATE_STRING.split(" ");
						
						tr = $("<tr></tr>");
						tr.append("<td>"+(i+1)+"</td>");
						tr.append("<td>"+result.LIST[i].CUSTOMER_ID+"</td>");
						
						if(result.LIST[i].LVL>1){
							tr.append("<td style='text-indent:"+((result.LIST[i].LVL-1)*20)+"px;'><a href='/LibraryService/board/viewArticle.do?BOARD="+BOARD+"&ARTICLE_ID="+result.LIST[i].ARTICLE_ID+"'> ➥   "+result.LIST[i].ARTICLE_TITLE+NEW+"</a></td>");
						}else{
							tr.append("<td style='text-indent:"+((result.LIST[i].LVL-1)*20)+"px;'><a href='/LibraryService/board/viewArticle.do?BOARD="+BOARD+"&ARTICLE_ID="+result.LIST[i].ARTICLE_ID+"'>"+result.LIST[i].ARTICLE_TITLE+NEW+"</a></td>");
						}
						
						if(recent){
							tr.append("<td>"+temp[1]+"</td>");
						}else{
							tr.append("<td>"+temp[0]+"</td>");
						}

						tr.append("<td>"+result.LIST[i].ARTICLE_VIEWS+"</td>");
						$("#listTable").append(tr);
					}
					paging(TOTAL);
				}
			},
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