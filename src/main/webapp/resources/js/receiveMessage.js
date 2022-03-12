//============================================================================================
//해당 날짜가 비교적 최근인지 아닌지 구분하는 메소드.
//============================================================================================
function isRecent(message_date_string){
	var temp1 = message_date_string.split(" ");
	var temp2 = temp1[0].split(".");
	var today = new Date();
	var Y = today.getFullYear(), M = today.getMonth(), D = today.getDate();
	var t1 = new Date(temp2[0],temp2[1]-1,temp2[2]), t2 = new Date(Y,M,D);
	
	if(t1>=t2){
		return true;
	}else{
		return false;
	}
}

$(document).ready(function(){
	//기본적으로 수신 메세지함을 검색하며, 메세지 송수신 날짜를 기준으로 내림차순으로 검색함.
	var message_box = "message_received";
	var orderby = "message_date DESC";
	var section=1, page=1;
	
	//메세지 삭제시에 모든 메세지를 클릭했는지 아닌지 여부를 나타내는 변수.
	var checkAll = false;
	var search ="";
	
	//페이지 로딩시 즉시 비동기식으로 메세지 목록을 요청함.
	receiveMessage(message_box, orderby, section, page);
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 요청하는 메소드, 최대 10개의 메세지를 한번에 요청할 수 있음.
	//============================================================================================
	function receiveMessage(message_box, orderby, section, page){
		$.ajax({
			"url":"/LibraryService/message/receiveMessage.do",
			"dataType":"json",
			"type":"post",
			"data":{
				"message_box":message_box,
				"orderby":orderby,
				"section":section,
				"page":page,
				"search":search
			},
			"success":function(result){
				//메세지 체크상태를 해제함.
				checkAll=false;
				if(result.flag=="true"){
					var total = result.total;
					var list = result.list;
					var i;
					$("#list").empty();
					$("#list").append("<tr class='row'><th class='col' id='select_all_button'>전체</th><th class='col' id='flag'>"+(message_box=="message_received"?"송신자":"수신자")+"</th><th id='title' class='col'>제목</th><th id='date' class='col'>보낸시각</th></tr>");
					
					console.log(message_box);
					
					for(i=0;i<list.length;i++){
						var recent = isRecent(list[i].message_date_string);
						var newFlag = recent==true?" <span class='new'>[new]</span>":"";
						var temp = list[i].message_date_string.split(" ");
						var tr = $("<tr></tr>");
						
						tr.append("<td><input type='checkbox' class='check' value='"+list[i].message_id+"'></td>");
						
						//송신함인지 수신함인지에 따라 각각 수신자, 송신자가 누구인지 나타냄.
						if(message_box == "message_received"){
							tr.append("<td>"+list[i].sender_id+"</td>");
						}else{
							tr.append("<td>"+list[i].receiver_id+"</td>");
						}
						
						tr.append("<td><a href='/LibraryService/message/readMessage.do?message_box="+message_box+"&message_id="+list[i].message_id+"'>"+list[i].message_title+newFlag+"</a></td>");
						
						
						//메세지 목록 수신에 성공했을때, 비교적 최신의 메세지라면 new 키워드를 추가하고, 해당 메세지의 송, 수신 시간을 표시함.
						//만약 비교적 최신의 메세지가 아니라면, 송, 수신 날짜를 표기함.
						if(recent){
							tr.append("<td>"+temp[1]+"</td>");
						}else{
							tr.append("<td>"+temp[0]+"</td>");
						}
						$("#list").append(tr);
					}
					paging(total);					
				}else{
					$("#list").empty();
					$("#list").append("<tr class='row'><td class='col' id='select_all_button'>전체</td><td class='col' id='flag'>"+(message_box=="message_received"?"송신자":"수신자")+"</td><td id='title' class='col'>제목</td><td id='date' class='col'>보낸시각</td></tr>");
					var tr = $("<tr></tr>");
					tr.append("<td colspan=4>"+result.content+"</td>");
					$("#list").append(tr);
					paging(0);
				}
			},
			"error": function(xhr, status, error) {
				  var err = JSON.parse(xhr.responseText);
				  alert(err.content);
	  		}
		});
	}
	
	
	
	
	
	//============================================================================================
	//메세지 삭제를 요청하는 메소드, 선택된 메세지를 일괄적으로 삭제함.
	//============================================================================================
	function deleteMessage(message_box){
		var message = $(".check:checked");
		var message_id="";
		var i;
		if(message.length==0){
			alert("삭제할 메세지를 선택해야합니다.");
			return;
		}
		
		//체크된 메세지들의 아이디를 공백문자로 구분하여 하나의 문자열로 합침.
		for(i=0; i<message.length; i++){
			if(i!=message.length-1){
				message_id+=$(message[i]).attr("value")+" ";
			}else{
				message_id+=$(message[i]).attr("value");
			}
		}
		
		
		
		
		//============================================================================================
		//백엔드 서버에 해당 메세지들의 삭제를 요청함.
		//============================================================================================
		$.ajax({
			"url":"/LibraryService/message/deleteAllMessage.do",
			"dataType":"json",
			"type":"post",
			"data":{
				"message_box":message_box,
				"message_id":message_id
			},
			"success":function(result){
				//메세지 삭제에 성공하면 메세지를 다시 읽음.
				if(result.flag=="true"){
					section=1;
					page=1;
					receiveMessage(message_box, orderby, section, page);
				}else{
					$("#list").empty();
					$("#list").append("<tr class='row'><td class='col' id='select_all_button'>전체</td><td class='col' id='flag'>"+(message_box=="message_received"?"송신자":"수신자")+"</td><td id='title' class='col'>제목</td><td id='date' class='col'>보낸시각</td></tr>");
					var tr = $("<tr></tr>");
					tr.append("<td colspan=4>"+result.content+"</td>");
					$("#list").append(tr);
					paging(0);
				}
			},
			"error": function(xhr, status, error) {
				  var err = JSON.parse(xhr.responseText);
				  alert(err.content);
	  		}
		});
	}
	
	
	
	
	
	
	//============================================================================================
	//해당 조건에 맞는 메세지들의 개수를 받아 페이징 처리하는 메소드.
	//============================================================================================
	function paging(total){
		var maxSection = Math.ceil(total/100);
		var maxPage = Math.ceil((total-(section-1)*100)/10)>=10?10:Math.ceil((total-(section-1)*100)/10);
		
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
				receiveMessage(message_box, orderby, section, page);
			})
			$("#paging").append(tag);
		}
		
		//해당 섹션에 존재하는 페이지수에 따라 페이징을 수행함.
		for(var i=1; i<=maxPage;i++){
			$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
			
			$("#paging").on("click","#idx"+i,function(){
				receiveMessage(message_box, orderby, section, $(this).attr("value"))
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
				receiveMessage(message_box, orderby, section, page);
			})
			$("#paging").append(tag);
		}
	}
	
	
	
	
	
	//============================================================================================
	//메세지 전체 선택 클릭시, 해당 페이지에 존재하는 모든 메세지들을 체크함.
	//현재 모든 메세지 체크 상태라면, 모든 메세지 체크를 해제함.
	//============================================================================================
	$(document).on("click","#select_all_button",function(){
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
	$(document).on("click","#delete_button",function(){
		deleteMessage(message_box);
	})
	
	
	
	
	
	//============================================================================================
	//메세지 송신함의 메세지를 조회하도록 설정하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#message_sent",function(){
		page = 1;
		section = 1;
		message_box = "message_sent";
		orderby = "message_date DESC";
		receiveMessage(message_box, orderby, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지 수신함의 메세지를 조회하도록 설정하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#message_received",function(){
		page = 1;
		section = 1;
		message_box = "message_received";
		orderby = "message_date DESC";
		receiveMessage(message_box, orderby, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지 검색 이벤트를 등록함. 해당 조건에 맞는 메세지들을 얻음.
	//============================================================================================
	$(document).on("click","#search_button",function(){
		page = 1;
		section = 1;
		search = $("#search").val();
		orderby = "message_date DESC";
		receiveMessage(message_box, orderby, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 송, 수신 날짜를 기준으로 오름차 혹은 내림차순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#date",function(){
		if(orderby == "message_date DESC"){
			orderby = "message_date ASC";
		}else{
			orderby = "message_date DESC";
		}
		receiveMessage(message_box, orderby, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 메세지 제목을 기준으로 오름차 혹은 내림차순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#title",function(){
		if(orderby == "message_title ASC"){
			orderby = "message_title DESC";
		}else{
			orderby = "message_title ASC";
		}
		receiveMessage(message_box, orderby, section, page);
	});
	
	
	
	
	
	//============================================================================================
	//메세지들을 송신자 혹은 수신자이름으로 오름차 혹은 내림차 순으로 조회하는 이벤트를 등록함.
	//============================================================================================
	$(document).on("click","#flag",function(){
		if(message_box == "message_received"){
			if(orderby == "sender_id ASC"){
				orderby = "sender_id DESC";
			}else{
				orderby = "sender_id ASC";
			}
		}else{
			if(orderby == "receiver_id ASC"){
				orderby = "receiver_id DESC";
			}else{
				orderby = "receiver_id ASC";
			}
		}
		receiveMessage(message_box, orderby, section, page);
	});
})