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
	
	var MESSAGE_BOX = "MESSAGE_RECEIVED";
	var ORDERBY = "MESSAGE_DATE DESC";
	var section=1, page=1;
	var checkAll = false;
	var SEARCH ="";
	
	receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	
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
						if(MESSAGE_BOX == "MESSAGE_RECEIVED"){
							tr.append("<td>"+LIST[i].SENDER_ID+"</td>");
						}else{
							tr.append("<td>"+LIST[i].RECEIVER_ID+"</td>");
						}
						
						tr.append("<td><a href='/LibraryService/message/readMessage.do?MESSAGE_BOX="+MESSAGE_BOX+"&MESSAGE_ID="+LIST[i].MESSAGE_ID+"'>"+LIST[i].MESSAGE_TITLE+NEW+"</a></td>");
						
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
	
	function deleteMessage(MESSAGE_BOX){
		var MESSAGE = $(".check:checked");
		var MESSAGE_ID="";
		var i;
		if(MESSAGE.length==0){
			alert("삭제할 메세지를 선택해야합니다.");
			return;
		}
		for(i=0; i<MESSAGE.length; i++){
			if(i!=MESSAGE.length-1){
				MESSAGE_ID+=$(MESSAGE[i]).attr("value")+" ";
			}else{
				MESSAGE_ID+=$(MESSAGE[i]).attr("value");
			}
		}
		$.ajax({
			"url":"/LibraryService/message/deleteMessage.do",
			"dataType":"JSON",
			"type":"POST",
			"data":{
				"MESSAGE_BOX":MESSAGE_BOX,
				"MESSAGE_ID":MESSAGE_ID
			},
			"success":function(result){
				if(result.FLAG=="TRUE"){
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
	
	$(document).on("click","#SELECT_ALL_BUTTON",function(){
		if(checkAll == false){
			checkAll=true;
			$(".check").prop("checked",true);
		}else{
			checkAll=false;
			$(".check").prop("checked",false);
		}
	})
	$(document).on("click","#DELETE_BUTTON",function(){
		deleteMessage(MESSAGE_BOX);
	})
	$(document).on("click","#MESSAGE_SENT",function(){
		page = 1;
		section = 1;
		MESSAGE_BOX = "MESSAGE_SENT";
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	$(document).on("click","#MESSAGE_RECEIVED",function(){
		page = 1;
		section = 1;
		MESSAGE_BOX = "MESSAGE_RECEIVED";
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	$(document).on("click","#SEARCH_BUTTON",function(){
		page = 1;
		section = 1;
		SEARCH = $("#SEARCH").val();
		ORDERBY = "MESSAGE_DATE DESC";
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	$(document).on("click","#DATE",function(){
		if(ORDERBY == "MESSAGE_DATE DESC"){
			ORDERBY = "MESSAGE_DATE ASC";
		}else{
			ORDERBY = "MESSAGE_DATE DESC";
		}
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
	$(document).on("click","#TITLE",function(){
		if(ORDERBY == "MESSAGE_TITLE ASC"){
			ORDERBY = "MESSAGE_TITLE DESC";
		}else{
			ORDERBY = "MESSAGE_TITLE ASC";
		}
		receiveMessage(MESSAGE_BOX, ORDERBY, section, page);
	});
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