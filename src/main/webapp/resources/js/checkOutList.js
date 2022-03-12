//============================================================================================
//도서대출 현황을 조회할 수 있도록 하는 자바스크립트 코드.
//============================================================================================
var section = 1, page = 1;
var search="";

//기본 설정은 대출을 시작한 날짜의 오름차순으로 조회함.
var orderby = "check_out_start_date DESC";
var now = new Date();
var year = now.getFullYear();

//기본적으로 현재 날짜로부터 6개월 전까지의 대출현황을 조회하지만, 수동으로 조절 가능.
var enddate = new Date(now.setMonth(now.getMonth()));
var startdate = new Date(now.setMonth(now.getMonth()-6));
var month,date,datelimit;




//============================================================================================
//대출현황 페이징을 수행하는 메소드, 서버로부터 대출현황 목록의 수 total을 전달받아 페이징을 처리함.
//============================================================================================
function paging(total){
	var max_section = Math.ceil(total/100);
	var max_page = Math.ceil((total-(section-1)*100)/10)>=10?10:Math.ceil((total-(section-1)*100)/10);

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
			getCheckOutList(section,page);
		})
		$("#paging").append(tag);
	}
	
	//해당 섹션에 존재하는 페이지수에 따라 페이징을 수행함.
	for(var i=1; i<=max_page;i++){
		$("#paging").append($("<a id='idx"+i+"' value='"+i+"'>"+i+"</a>"));		
		$("#paging").on("click","#idx"+i,function(){
			getCheckOutList(section,$(this).attr("value"));
			page = $(this).attr("value");
		})
	}
	
	$("#paging a").removeClass("active");
	$("#idx"+page).addClass("active");
	
	//아직 마지막 섹션이 아니라면, 다음 섹션으로 넘어갈 수 있도록 다음 버튼을 생성함.
	if(section<max_section){
		tag = $("<a id='next'>다음</a>");
		$("#paging").on("click","#next",function(){
			section++;
			page=1;
			getCheckOutList(section,page);
		})
		$("#paging").append(tag);
	}
}




//============================================================================================
//대출현황 목록을 백엔드 서버에 요청하는 메소드.
//============================================================================================
function getCheckOutList(section,page){
	$.ajax({
		"url":"/LibraryService/book/checkOutList.do",
		"type":"post",
		"dataType":"json",
		"data":{
			"orderby":orderby,
			"section":section,
			"page":page,
			"search":search,
			
			//플래그 변수는 ISBN코드, 사용자 ID 등등, 어떤 기준으로 검색하는 것인지를 나타냄.
			"flag":$("#flag").val(),
			
			//특정 날짜의 00:00:00 시간부터 특정 날짜의 23:59:59 시간까지의 정보를 조회함. 
			"start_date":$("#start_date").val()+" 00:00:00",
			"end_date":$("#end_date").val()+" 23:59:59"
		},
		"success":function(result){
			//조회에 성공한 대출현황수에 따라 자동으로 페이징함.
			var total = result.total;
			
			//대출현황이 없거나, 할수 없는 경우 대출 현황 정보가 존재하지 않다고 표시함.
			if(result.flag == "false"||total==0){
				section=1;
				page=1;
				$("#listTable").empty();
				var tr = $("<tr></tr>");
				tr.append("<th id='book_isbn'>ISBN</th>");
				tr.append("<th id='book_name'>책</th>");
				tr.append("<th id='customer_id'>ID</th>");
				tr.append("<th id='customer_name'>대출자</th>");
				tr.append("<th id='check_out_start_date'>대출 시각</th>");
				tr.append("<th id='check_out_end_date'>반납 기한</th>");
				tr.append("<th id='send_message' colspan='2'>연체 알림</th>");
				$("#listTable").append(tr);
				tr = $("<tr></tr>");
				tr.append("<td colspan=8>대출 현황 정보가 존재하지 않습니다.</td>");
				$("#listTable").append(tr);
				paging(0);
			}else{
				//대출 현황 조회에 성공한 경우, 대출현황을 나열함.
				$("#listTable").empty();
				var tr = $("<tr></tr>");
				tr.append("<th id='book_isbn'>ISBN</th>");
				tr.append("<th id='book_name'>책</th>");
				tr.append("<th id='customer_id'>ID</th>");
				tr.append("<th id='customer_name'>대출자</th>");
				tr.append("<th id='check_out_start_date'>대출 시각</th>");
				tr.append("<th id='check_out_end_date'>반납 기한</th>");
				tr.append("<th id='send_message' colspan='2'>연체 알림</th>");
				$("#listTable").append(tr);
				
				for(var i=0; i<result.list.length;i++){
					//해당 대출정보가 비교적 최근의 정보인지 구분함.
					var recent = isRecent(result.list[i].check_out_start_date_string);
					var temp = result.list[i].check_out_start_date_string.split(" ");
					tr = $("<tr></tr>");
					tr.append("<td><a>"+result.list[i].book_isbn+"</a></td>");
					tr.append("<td>"+result.list[i].book_name+"</td>");
					tr.append("<td>"+result.list[i].customer_id+"</td>");
					tr.append("<td>"+result.list[i].customer_name+"</td>");						
					
					//해당 대출정보가 비교적 최근의 것이라면, 시간을, 아니라면 날짜를 표기함.
					if(recent){
						tr.append("<td>"+temp[1]+"</td>");
					}else{
						tr.append("<td>"+temp[0]+"</td>");
					}

					//해당 대출이 연체되었다면, 며칠 연체되었는지 나타냄.
					if(result.list[i].overdue>0){
						tr.append("<td>[ 연체 "+result.list[i].overdue+"일]</td>");
					}else{
						//연체되지 않았다면 반납 기한을 표시함.
						temp = result.list[i].check_out_end_date_string.split(" ");
						tr.append("<td>"+temp[0]+"</td>");
					}
					
					//각 대출에 대해 대출기한 연장, 도서 반납이 가능하도록 함.
					tr.append("<td><a value='"+result.list[i].check_out_id+"' onClick='renewBook(this)'>연장</a></td>");
					tr.append("<td><a value='"+result.list[i].check_out_id+"' onClick='returnBook(this)'>반납</a></td>");
					$("#listTable").append(tr);
				}
				paging(total);
			}
		},
		"error": function(xhr, status, error) {
			var err = JSON.parse(xhr.responseText);
			alert(err.content);
			section=1;
			page=1;
			search=$("#search").val();
			getCheckOutList(section,page);
  		}
	})
}




//============================================================================================
//도서 반납 요청을 수행하는 메소드.
//============================================================================================
function returnBook(obj){
	var flag = confirm("도서를 반납합니다.");
	if(flag){
		$.ajax({
			"url":"/LibraryService/book/returnBook.do",
			"type":"post",
			"dataType":"json",
			"data":{
				"check_out_id":$(obj).attr("value")
			},
			"success":function(result){
				if(result.flag!="true"){
					alert(result.content);
				}else{
					section = 1;
					page = 1;
					getCheckOutList(section,page);
				}
			},
			"error": function(xhr, status, error) {
				var err = JSON.parse(xhr.responseText);
				alert(err.content);
				section=1;
				page=1;
				search=$("#search").val();
				getCheckOutList(section,page);
	  		}
		});
	}
}





//============================================================================================
//대출기한 연장을 요청하는 메소드.
//============================================================================================
function renewBook(obj){
	var flag = confirm("대출기한을 연장합니다.");
	if(flag){
		$.ajax({
			"url":"/LibraryService/book/renewBook.do",
			"type":"post",
			"dataType":"json",
			"data":{
				"check_out_id":$(obj).attr("value")
			},
			"success":function(result){
				if(result.flag!="true"){
					alert(result.content);
				}else{
					section = 1;
					page = 1;
					getCheckOutList(section,page);
				}
			},
			"error": function(xhr, status, error) {
				var err = JSON.parse(xhr.responseText);
				alert(err.content);
				section=1;
				page=1;
				search=$("#search").val();
				getCheckOutList(section,page);
	  		}
		});
	}
}





//============================================================================================
//해당 날짜가 비교적 최근의 날짜인지 아닌지를 구분하는 메소드.
//============================================================================================
function isRecent(date_string){
	var temp1 = date_string.split(" ");
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
	//현재 날짜 이후로는 날짜를 선택할 수 없도록 제한을 설정함.
    if(startdate.getMonth()+1<10){month = "0"+(startdate.getMonth()+1);
    }else{month = startdate.getMonth()+1;}
    if(startdate.getDate()<10){date = "0"+(startdate.getDate());
    }else{date = startdate.getDate();}
    year = startdate.getFullYear();
    datelimit = year+"-"+month+"-"+date;
    
    //대출현황 조회 시작날짜의 기본값은 현재 날짜로부터 6달전.
    $("#start_date").attr({
    	"value":datelimit
    });
    
   
    if(enddate.getMonth()+1<10){month = "0"+(enddate.getMonth()+1);
    }else{month = enddate.getMonth()+1;}
    if(enddate.getDate()<10){date = "0"+(enddate.getDate());
    }else{date = enddate.getDate();}
    year = enddate.getFullYear();
    datelimit = year+"-"+month+"-"+date;
    
    //대출현황 조회의 마지막 날짜의 기본값은 현재날짜.
    $("#end_date").attr({
    	"value":datelimit,
    	"max":datelimit
    });
    $("#start_date").attr({
    	"max":datelimit
    });
	
    
    
    
    
    //============================================================================================
    //각 테이블 컬럼의 헤더를 클릭하면 그 기준에 맞게 오름차, 내림차순으로 조회함.
 	//============================================================================================
	$(document).on("click","#book_isbn",function(){if(orderby == "book_isbn ASC"){orderby = "book_isbn DESC";}else{orderby = "book_isbn ASC";}getCheckOutList(section,page);});
	$(document).on("click","#book_name",function(){if(orderby == "book_name ASC"){orderby = "book_name DESC";}else{orderby = "book_name ASC";}getCheckOutList(section,page);});
	$(document).on("click","#customer_id",function(){if(orderby == "customer_id ASC"){orderby = "customer_id DESC";}else{orderby = "customer_id ASC";}getCheckOutList(section,page);});
	$(document).on("click","#customer_name",function(){if(orderby == "customer_name ASC"){orderby = "customer_name DESC";}else{orderby = "customer_name ASC";}getCheckOutList(section,page);});
	$(document).on("click","#check_out_start_date",function(){if(orderby == "check_out_start_date DESC"){orderby = "check_out_start_date ASC";}else{orderby = "check_out_start_date DESC";}getCheckOutList(section,page);});
	$(document).on("click","#check_out_end_date",function(){if(orderby == "check_out_end_date DESC"){orderby = "check_out_end_date ASC";}else{orderby = "check_out_end_date DESC";}getCheckOutList(section,page);});

	//페이지 로딩후에 게시글 목록을 요청함.
	getCheckOutList(section,page);
	
	
	
	
	//============================================================================================
	//게시글 버튼 클릭시에 게시글을 조건에 맞게 다시 검색함.
	//============================================================================================
	$(document).on("click","#search_button",function(){
		section=1;
		page=1;
		search=$("#search").val();
		getCheckOutList(section,page);
	});
	
	
	
	
	
	//============================================================================================
	//관리자의 경우 도서 대출 연체들에 대하여 연체 정보를 일괄적으로 전송할 수 있음.
	//============================================================================================
	$(document).on("click","#send_message",function(){
		var flag = confirm("연체된 대출정보에 대한 알림메세지를 일괄 전송합니다.");
		if(flag){
			$.ajax({
				"url":"/LibraryService/book/sendMessage.do",
				"type":"post",
				"dataType":"json",
				"success":function(result){
					alert(result.content);
				},
				"error": function(xhr, status, error) {
					var err = JSON.parse(xhr.responseText);
					alert(err.content);
					
					section = 1;
					page = 1;
					
					$("#listTable").empty();
					
					var tr = $("<tr></tr>");
					tr.append("<th id='book_isbn'>ISBN</th>");
					tr.append("<th id='book_name'>책</th>");
					tr.append("<th id='customer_id'>ID</th>");
					tr.append("<th id='customer_name'>대출자</th>");
					tr.append("<th id='check_out_start_date'>대출 시각</th>");
					tr.append("<th id='check_out_end_date'>반납 기한</th>");
					tr.append("<th id='send_message' colspan='2'>연체 알림</th>");
					
					$("#listTable").append(tr);
					tr = $("<tr></tr>");
					
					tr.append("<td colspan=8>대출 현황 정보가 존재하지 않습니다.</td>");
					$("#listTable").append(tr);
					
					paging(0);
		  		}
			});
		}
	});
});