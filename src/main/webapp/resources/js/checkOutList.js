var SECTION = 1, PAGE = 1;
var SEARCH="";
var ORDERBY = "CHECK_OUT_START_DATE DESC";
var now = new Date();
var year = now.getFullYear();
var enddate = new Date(now.setMonth(now.getMonth()));
var startdate = new Date(now.setMonth(now.getMonth()-6));
var month,date,datelimit;

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
			getCheckOutList(SECTION,PAGE);
		})
		$("#paging").append(tag);
	}
	for(var i=1; i<=maxPAGE;i++){
		$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));
		
		$("#paging").on("click","#idx"+i,function(){
			getCheckOutList(SECTION,$(this).attr("value"));
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
			getCheckOutList(SECTION,PAGE);
		})
		$("#paging").append(tag);
	}
}

function getCheckOutList(SECTION,PAGE){
	$.ajax({
		"url":"/LibraryService/book/listCheckOuts.do",
		"type":"post",
		"dataType":"json",
		"data":{
			"ORDERBY":ORDERBY,
			"SECTION":SECTION,
			"PAGE":PAGE,
			"SEARCH":SEARCH,
			"FLAG":$("#FLAG").val(),
			"START_DATE":$("#START_DATE").val()+" 00:00:00",
			"END_DATE":$("#END_DATE").val()+" 23:59:59"
		},
		"success":function(result){
			
			var TOTAL = result.TOTAL;
			if(result.FLAG == "FALSE"||TOTAL==0){
				SECTION=1;
				PAGE=1;
				$("#listTable").empty();
				var tr = $("<tr></tr>");
				tr.append("<th id='BOOK_ISBN'>ISBN</th>");
				tr.append("<th id='BOOK_NAME'>책</th>");
				tr.append("<th id='CUSTOMER_ID'>ID</th>");
				tr.append("<th id='CUSTOMER_NAME'>대출자</th>");
				tr.append("<th id='CHECK_OUT_START_DATE'>대출 시각</th>");
				tr.append("<th id='CHECK_OUT_END_DATE'>반납 기한</th>");
				tr.append("<th id='SEND_MESSAGE' colspan='2'>연체 알림</th>");
				$("#listTable").append(tr);
				tr = $("<tr></tr>");
				tr.append("<td colspan=8>대출 현황 정보가 존재하지 않습니다.</td>");
				$("#listTable").append(tr);
				paging(0);
			}else{
				$("#listTable").empty();
				var tr = $("<tr></tr>");
				tr.append("<th id='BOOK_ISBN'>ISBN</th>");
				tr.append("<th id='BOOK_NAME'>책</th>");
				tr.append("<th id='CUSTOMER_ID'>ID</th>");
				tr.append("<th id='CUSTOMER_NAME'>대출자</th>");
				tr.append("<th id='CHECK_OUT_START_DATE'>대출 시각</th>");
				tr.append("<th id='CHECK_OUT_END_DATE'>반납 기한</th>");
				tr.append("<th id='SEND_MESSAGE' colspan='2'>연체 알림</th>");
				$("#listTable").append(tr);
				for(var i=0; i<result.LIST.length;i++){
					var recent = isRecent(result.LIST[i].CHECK_OUT_START_DATE_STRING);
					var temp = result.LIST[i].CHECK_OUT_START_DATE_STRING.split(" ");
					
					tr = $("<tr></tr>");
					tr.append("<td><a>"+result.LIST[i].BOOK_ISBN+"</a></td>");
					tr.append("<td>"+result.LIST[i].BOOK_NAME+"</td>");
					tr.append("<td>"+result.LIST[i].CUSTOMER_ID+"</td>");
					tr.append("<td>"+result.LIST[i].CUSTOMER_NAME+"</td>");						
					
					if(recent){
						tr.append("<td>"+temp[1]+"</td>");
					}else{
						tr.append("<td>"+temp[0]+"</td>");
					}

					
					
					
					if(result.LIST[i].OVERDUE>0){
						tr.append("<td>[ 연체 "+result.LIST[i].OVERDUE+"일]</td>");
					}else{
						temp = result.LIST[i].CHECK_OUT_END_DATE_STRING.split(" ");
						tr.append("<td>"+temp[0]+"</td>");
					}
					
					
					tr.append("<td><a value='"+result.LIST[i].CHECK_OUT_ID+"' onClick='renewBook(this)'>연장</a></td>");
					tr.append("<td><a value='"+result.LIST[i].CHECK_OUT_ID+"' onClick='returnBook(this)'>반납</a></td>");
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
			tr.append("<th id='BOOK_ISBN'>ISBN</th>");
			tr.append("<th id='BOOK_NAME'>책</th>");
			tr.append("<th id='CUSTOMER_ID'>ID</th>");
			tr.append("<th id='CUSTOMER_NAME'>대출자</th>");
			tr.append("<th id='CHECK_OUT_START_DATE'>대출 시각</th>");
			tr.append("<th id='CHECK_OUT_END_DATE'>반납 기한</th>");
			tr.append("<th id='SEND_MESSAGE' colspan='2'>연체 알림</th>");
			$("#listTable").append(tr);
			tr = $("<tr></tr>");
			tr.append("<td colspan=8>대출 현황 정보가 존재하지 않습니다.</td>");
			$("#listTable").append(tr);
			paging(0);
		}
	})
}

function returnBook(obj){
	var flag = confirm("도서를 반납합니다.");
	if(flag){
		$.ajax({
			"url":"/LibraryService/book/returnBook.do",
			"type":"POST",
			"dataType":"json",
			"data":{
				"CHECK_OUT_ID":$(obj).attr("value")
			},
			"success":function(result){
				if(result.FLAG!="TRUE"){
					alert(result.CONTENT);
				}else{
					SECTION = 1;
					PAGE = 1;
					getCheckOutList(SECTION,PAGE);
				}
			},
			"error":function(){
				SECTION = 1;
				PAGE = 1;
				getCheckOutList(SECTION,PAGE);
			}
		});
	}
}

function renewBook(obj){
	var flag = confirm("대출기한을 연장합니다.");
	if(flag){
		$.ajax({
			"url":"/LibraryService/book/renewBook.do",
			"type":"POST",
			"dataType":"json",
			"data":{
				"CHECK_OUT_ID":$(obj).attr("value")
			},
			"success":function(result){
				if(result.FLAG!="TRUE"){
					alert(result.CONTENT);
				}else{
					SECTION = 1;
					PAGE = 1;
					getCheckOutList(SECTION,PAGE);
				}
			},
			"error":function(){
				SECTION = 1;
				PAGE = 1;
				getCheckOutList(SECTION,PAGE);
			}
		});
	}
}

function isRecent(DATE_STRING){
	var temp1 = DATE_STRING.split(" ");
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

    if(startdate.getMonth()+1<10){month = "0"+(startdate.getMonth()+1);
    }else{month = startdate.getMonth()+1;}
    if(startdate.getDate()<10){date = "0"+(startdate.getDate());
    }else{date = startdate.getDate();}
    year = startdate.getFullYear();
    datelimit = year+"-"+month+"-"+date;
    
    $("#START_DATE").attr({
    	"value":datelimit
    });
    
   
    if(enddate.getMonth()+1<10){month = "0"+(enddate.getMonth()+1);
    }else{month = enddate.getMonth()+1;}
    if(enddate.getDate()<10){date = "0"+(enddate.getDate());
    }else{date = enddate.getDate();}
    year = enddate.getFullYear();
    datelimit = year+"-"+month+"-"+date;
    
    $("#END_DATE").attr({
    	"value":datelimit,
    	"max":datelimit
    });
    $("#START_DATE").attr({
    	"max":datelimit
    });
	
	$(document).on("click","#BOOK_ISBN",function(){if(ORDERBY == "BOOK_ISBN ASC"){ORDERBY = "BOOK_ISBN DESC";}else{ORDERBY = "BOOK_ISBN ASC";}getCheckOutList(SECTION,PAGE);});
	$(document).on("click","#BOOK_NAME",function(){if(ORDERBY == "BOOK_NAME ASC"){ORDERBY = "BOOK_NAME DESC";}else{ORDERBY = "BOOK_NAME ASC";}getCheckOutList(SECTION,PAGE);});
	$(document).on("click","#CUSTOMER_ID",function(){if(ORDERBY == "CUSTOMER_ID ASC"){ORDERBY = "CUSTOMER_ID DESC";}else{ORDERBY = "CUSTOMER_ID ASC";}getCheckOutList(SECTION,PAGE);});
	$(document).on("click","#CUSTOMER_NAME",function(){if(ORDERBY == "CUSTOMER_NAME ASC"){ORDERBY = "CUSTOMER_NAME DESC";}else{ORDERBY = "CUSTOMER_NAME ASC";}getCheckOutList(SECTION,PAGE);});
	$(document).on("click","#CHECK_OUT_START_DATE",function(){if(ORDERBY == "CHECK_OUT_START_DATE DESC"){ORDERBY = "CHECK_OUT_START_DATE ASC";}else{ORDERBY = "CHECK_OUT_START_DATE DESC";}getCheckOutList(SECTION,PAGE);});
	$(document).on("click","#CHECK_OUT_END_DATE",function(){if(ORDERBY == "CHECK_OUT_END_DATE DESC"){ORDERBY = "CHECK_OUT_END_DATE ASC";}else{ORDERBY = "CHECK_OUT_END_DATE DESC";}getCheckOutList(SECTION,PAGE);});

	
	
	getCheckOutList(SECTION,PAGE);
	
	
	$(document).on("click","#SEARCH_BUTTON",function(){
		SECTION=1;
		PAGE=1;
		SEARCH=$("#SEARCH").val();
		getCheckOutList(SECTION,PAGE);
	});
	
	$(document).on("click","#SEND_MESSAGE",function(){
		var flag = confirm("연체된 대출정보에 대한 알림메세지를 일괄 전송합니다.");
		if(flag){
			$.ajax({
				"url":"/LibraryService/book/sendMessage.do",
				"type":"POST",
				"dataType":"json",
				"data":{
					
				},
				"success":function(result){
					alert(result.CONTENT);
				},
				"error":function(){
					SECTION = 1;
					PAGE = 1;
					getCheckOutList(SECTION,PAGE);
				}
			});
		}
	});
	
});