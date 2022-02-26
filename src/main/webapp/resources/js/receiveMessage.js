//============================================================================================
//해당 날짜가 비교적 최근인지 아닌지 구분하는 메소드.
//============================================================================================
function isRecent(MESSAGE_DATE_STRING){
	var temp1 = MESSAGE_DATE_STRING.split(" ");
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
	//기본적으로 수신 메세지함을 검색하며, 메세지 송수신 날짜를 기준으로 내림차순으로 검색함.
	var MESSAGE_BOX = "MESSAGE_RECEIVED";
	var ORDERBY = "MESSAGE_DATE DESC";
	var section=1, page=1;
	
	//메세지 삭제시에 모든 메세지를 클릭했는지 아닌지 여부를 나타내는 변수.
	var checkAll = false;
	var SEARCH ="";
	
	//페이지 로딩시 즉시 비동기식으로 메세지 목록을 요청함.
	receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 요청하는 메소드, 최대 10개의 메세지를 한번에 요청할 수 있음.
	//============================================================================================
	function receiveMessage(MESSAGE_BOX, ORDERBY, section, page){
		$.ajax({
			"url":"/LibraryService/message/receiveMessage.do",
			"dataType":"JSON",
			"type":"POST",
			"data":{
				"MESSAGE_BOX":MESSAGE_BOX,
				"ORDERBY":ORDERBY,
				"SECTION":section,
				"PAGE":page,
				"SEARCH":SEARCH
			},
			"success":function(result){
				//메세지 체크상태를 해제함.
				checkAll=false;
				if(result.FLAG=="TRUE"){
					var TOTAL = result.TOTAL;
					var LIST = result.LIST;
					var i;
					$("#LIST").empty();
					$("#LIST").append("<tr class='row'><th class='col' id='SELECT_ALL_BUTTON'>전체</th><th class='col' id='FLAG'>"+(MESSAGE_BOX=="MESSAGE_RECEIVED"?"송신자":"수신자")+"</th><th id='TITLE' class='col'>제목</th><th id='DATE' class='col'>보낸시각</th></tr>");
					

					for(i=0;i<LIST.length;i++){
						var recent = isRecent(LIST[i].MESSAGE_DATE_STRING);
						var NEW = recent==true?" <span class='new'>[new]</span>":"";
						var temp = LIST[i].MESSAGE_DATE_STRING.split(" ");
						var tr = $("<tr></tr>");
						
						tr.append("<td><input type='checkbox' class='check' value='"+LIST[i].MESSAGE_ID+"'></td>");
						
						//송신함인지 수신함인지에 따라 각각 수신자, 송신자가 누구인지 나타냄.
						if(MESSAGE_BOX == "MESSAGE_RECEIVED"){
							tr.append("<td>"+LIST[i].SENDER_ID+"</td>");
						}else{
							tr.append("<td>"+LIST[i].RECEIVER_ID+"</td>");
						}
						
						tr.append("<td><a href='/LibraryService/message/readMessage.do?MESSAGE_BOX="+MESSAGE_BOX+"&MESSAGE_ID="+LIST[i].MESSAGE_ID+"'>"+LIST[i].MESSAGE_TITLE+NEW+"</a></td>");
						
						
						//메세지 목록 수신에 성공했을때, 비교적 최신의 메세지라면 new 키워드를 추가하고, 해당 메세지의 송, 수신 시간을 표시함.
						//만약 비교적 최신의 메세지가 아니라면, 송, 수신 날짜를 표기함.
						if(recent){
							tr.append("<td>"+temp[1]+"</td>");
						}else{
							tr.append("<td>"+temp[0]+"</td>");
						}
						$("#LIST").append(tr);
					}
					paging(TOTAL);					
				}else{
					$("#LIST").empty();
					$("#LIST").append("<tr class='row'><td class='col' id='SELECT_ALL_BUTTON'>전체</td><td class='col' id='FLAG'>"+(MESSAGE_BOX=="MESSAGE_RECEIVED"?"송신자":"수신자")+"</td><td id='TITLE' class='col'>제목</td><td id='DATE' class='col'>보낸시각</td></tr>");
					var tr = $("<tr></tr>");
					tr.append("<td colspan=4>"+result.CONTENT+"</td>");
					$("#LIST").append(tr);
					paging(0);
				}
			},
			"error":function(){
				alert("에러");				
			}
		});
	}
	
	
	
	
	
	//============================================================================================
	//메세지 삭제를 요청하는 메소드, 선택된 메세지를 일괄적으로 삭제함.
	//============================================================================================
	function deleteMessage(MESSAGE_BOX){
		var MESSAGE = $(".check:checked");
		var MESSAGE_ID="";
		var i;
		if(MESSAGE.length==0){
			alert("삭제할 메세지를 선택해야합니다.");
			return;
		}
		
		//체크된 메세지들의 아이디를 공백문자로 구분하여 하나의 문자열로 합침.
		for(i=0; i<MESSAGE.length; i++){
			if(i!=MESSAGE.length-1){
				MESSAGE_ID+=$(MESSAGE[i]).attr("value")+" ";
			}else{
				MESSAGE_ID+=$(MESSAGE[i]).attr("value");
			}
		}
		
		
		
		
		//============================================================================================
		//백엔드 서버에 해당 메세지들의 삭제를 요청함.
		//============================================================================================
		$.ajax({
			"url":"/LibraryService/message/deleteMessage.do",
			"dataType":"JSON",
			"type":"POST",
			"data":{
				"MESSAGE_BOX":MESSAGE_BOX,
				"MESSAGE_ID":MESSAGE_ID
			},
			"success":function(result){
				//메세지 삭제에 성공하면 메세지를 다시 읽음.
				if(result.FLAG=="TRUE"){
					section=1;
					page=1;
					receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
				}else{
					$("#LIST").empty();
					$("#LIST").append("<tr class='row'><td class='col' id='SELECT_ALL_BUTTON'>전체</td><td class='col' id='FLAG'>"+(MESSAGE_BOX=="MESSAGE_RECEIVED"?"송신자":"수신자")+"</td><td id='TITLE' class='col'>제목</td><td id='DATE' class='col'>보낸시각</td></tr>");
					var tr = $("<tr></tr>");
					tr.append("<td colspan=4>"+result.CONTENT+"</td>");
					$("#LIST").append(tr);
					paging(0);
				}
			},
			"error":function(){
				alert("에러");
			}
		});
	}
	
	
	
	
	
	
	//============================================================================================
	//해당 조건에 맞는 메세지들의 개수를 받아 페이징 처리하는 메소드.
	//============================================================================================
	function paging(TOTAL){
		var maxSection = Math.ceil(TOTAL/100);
		var maxPage = Math.ceil((TOTAL-(section-1)*100)/10)>=10?10:Math.ceil((TOTAL-(section-1)*100)/10);
		
		$("#paging").off();
		
		$("#paging").empty();
		
		var tag;
		
		//섹션이 2이상이라면, 101번째 이상의 게시글을 조회하고 있는 것이므로
		//그 이전 100개의 리스트를 얻을 수 있도록 이전버튼을 생성함.
		if(section>=2){
			tag = $("<a id='prev'>이전</a>");
			$("#paging").on("click","#prev",function(){
				section--;
				page=1;
				receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
			})
			$("#paging").append(tag);
		}
		
		//해당 섹션에 존재하는 페이지수에 따라 페이징을 수행함.
		for(var i=1; i<=maxPage;i++){
			$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
			
			$("#paging").on("click","#idx"+i,function(){
				receiveMessage(MESSAGE_BOX, ORDERBY, section, $(this).attr("value"))
				page = $(this).attr("value");
			})
		}
		$("#paging a").removeClass("active");
		$("#idx"+page).addClass("active");
		
		//아직 마지막 섹션이 아니라면, 다음 섹션으로 넘어갈 수 있도록 다음 버튼을 생성함.
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
	
	
	
	
	
	//============================================================================================
	//메세지 전체 선택 클릭시, 해당 페이지에 존재하는 모든 메세지들을 체크함.
	//현재 모든 메세지 체크 상태라면, 모든 메세지 체크를 해제함.
	//============================================================================================
	$(document).on("click","#SELECT_ALL_BUTTON",function(){
		if(checkAll == false){
			checkAll=true;
			$(".check").prop("checked",true);
		}else{
			checkAll=false;
			$(".check").prop("checked",false);
		}
	});
	
	
	
	
	
	//============================================================================================
	//선택된 메세지들을 삭제하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#DELETE_BUTTON",function(){
		deleteMessage(MESSAGE_BOX);
	})
	
	
	
	
	
	//============================================================================================
	//메세지 송신함의 메세지를 조회하도록 설정하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#MESSAGE_SENT",function(){
		page = 1;
		section = 1;
		MESSAGE_BOX = "MESSAGE_SENT";
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지 수신함의 메세지를 조회하도록 설정하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#MESSAGE_RECEIVED",function(){
		page = 1;
		section = 1;
		MESSAGE_BOX = "MESSAGE_RECEIVED";
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지 검색 이벤트를 등록함. 해당 조건에 맞는 메세지들을 얻음.
	//============================================================================================
	$(document).on("click","#SEARCH_BUTTON",function(){
		page = 1;
		section = 1;
		SEARCH = $("#SEARCH").val();
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 송, 수신 날짜를 기준으로 오름차 혹은 내림차순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#DATE",function(){
		if(ORDERBY == "MESSAGE_DATE DESC"){
			ORDERBY = "MESSAGE_DATE ASC";
		}else{
			ORDERBY = "MESSAGE_DATE DESC";
		}
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 메세지 제목을 기준으로 오름차 혹은 내림차순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#TITLE",function(){
		if(ORDERBY == "MESSAGE_TITLE ASC"){
			ORDERBY = "MESSAGE_TITLE DESC";
		}else{
			ORDERBY = "MESSAGE_TITLE ASC";
		}
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 송신자 혹은 수신자이름으로 오름차 혹은 내림차 순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#FLAG",function(){
		if(MESSAGE_BOX == "MESSAGE_RECEIVED"){
			if(ORDERBY == "SENDER_ID ASC"){
				ORDERBY = "SENDER_ID DESC";
			}else{
				ORDERBY = "SENDER_ID ASC";
			}
		}else{
			if(ORDERBY == "RECEIVER_ID ASC"){
				ORDERBY = "RECEIVER_ID DESC";
			}else{
				ORDERBY = "RECEIVER_ID ASC";
			}
		}
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
})