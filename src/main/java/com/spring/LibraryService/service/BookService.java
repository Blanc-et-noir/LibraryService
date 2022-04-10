package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.BookDAOInterface;
import com.spring.LibraryService.exception.book.ExhaustedRenewCountException;
import com.spring.LibraryService.exception.book.InvalidBookISBNException;
import com.spring.LibraryService.exception.book.InvalidCheckOutIDException;
import com.spring.LibraryService.exception.book.RunOutOfBookNumberException;
import com.spring.LibraryService.vo.CustomerVO;

@Service("bookService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={
		Exception.class,
		
		InvalidBookISBNException.class,
		InvalidCheckOutIDException.class,
		
		RunOutOfBookNumberException.class,
		
		ExhaustedRenewCountException.class
	}
)
public class BookService implements BookServiceInterface{
	
	@Autowired
	private BookDAOInterface bookDAO;
	
	//============================================================================================
	//입력받은 정보를 토대로 도서 대출 신청을 처리하는 메소드.
	//============================================================================================
	public HashMap checkOut(HashMap param) throws InvalidBookISBNException,RunOutOfBookNumberException, Exception{
		HashMap result = new HashMap();
		//해당 도서가 있는지 검사
		bookDAO.checkBookISBN(param);
		
		//해당 도서 재고량 여유가 있는지 검사
		bookDAO.checkBookNumber(param);
		
		//해당 사용자의 대출현황이 3개 미만인가 검사.
		List list = bookDAO.getCheckOutList(param);
		if(list.size()>=3) {
			result.put("flag", "false");
			result.put("content", "대출한도 초과");
			return result;
		}else {
			//현재 대출중 연체한 책이 있는가 검사.
			HashMap map;
			for(int i=0; i<list.size(); i++) {
				map = (HashMap) list.get(i);
				double time = Double.parseDouble(String.valueOf(map.get("diff")));
				if(time<0) {
					result.put("flag", "false");
					result.put("content", "\""+map.get("book_name")+"\" 대출 연체");
					return result;
				}
			}
			
			//대출가능시각 이후에 책을 대출하는 것인지 검사.
			map = bookDAO.checkCheckOutDate(param);
			double time = Double.parseDouble(String.valueOf(map.get("diff")));
			
			if(time<0) {
				result.put("flag", "false");
				result.put("content", map.get("check_out_date_string")+" 이후부터 대출이 가능");
				return result;
			}
			
			//책의 재고량 1 감소시킴.
			bookDAO.decreaseBookNum(param);
			
			//책대출현황에 해당 대출정보 추가.
			bookDAO.insertCheckOut(param);
			
			result.put("flag", "true");
			result.put("content", "대출 성공");
			return result;
		}
	}

	//============================================================================================
	//대출한 도서에 대해 반납을 처리하는 메소드.
	//============================================================================================
	public void returnBook(HashMap param) throws InvalidBookISBNException, InvalidCheckOutIDException, Exception{
		//반납하기전 먼저 현재날짜와 반납날짜를 비교함, 연체 여부 확인.
		HashMap map = bookDAO.isOverdue(param);
		int overdue = Integer.parseInt(String.valueOf(map.get("overdue")+""));
		String customer_id = (String)map.get("customer_id");

		//해당 도서가 있는지 검사
		bookDAO.checkBookISBN(param);

		//해당 대출정보가 있는지 검사
		bookDAO.checkCheckOutID(param);
		
		//연체했으면 사용자의 대출가능시각과 현재날짜중 더 최신의값 + 연체일수 결과를 대출가능시각으로 업데이트.
		if(overdue>0) {
			param.put("overdue", overdue);
			param.put("customer_id", customer_id);
			bookDAO.updateCheckOutDate(param);
		}
		
		//해당 도서의 재고량을 1 증가시킴.
		bookDAO.increaseBookNum(param);
		
		//대출현황에서 반납여부를 Y로 설정.
		bookDAO.returnBook(param);
	}
	
	//============================================================================================
	//도서 대출 반납기한 연장 요청을 처리하는 메소드.
	//============================================================================================
	public void renewBook(HashMap param, HttpServletRequest request) throws InvalidBookISBNException, ExhaustedRenewCountException, Exception{
		//해당 도서가 있는지 검사
		bookDAO.checkBookISBN(param);
		
		//연장 횟수가 남아있는지 검사
		bookDAO.checkRenewCount(param);
		
		CustomerVO customer = (CustomerVO)(request.getSession().getAttribute("customer"));
		if(customer.getKind_number()==0) {
			//관리자가 연장을 신청
			bookDAO.renewBookAsAdmin(param);
		}else {
			//대출자가 연장을 신청
			param.put("customer_id", customer.getCustomer_id());
			bookDAO.renewBookAsCustomer(param);
		}
	}

	//============================================================================================
	//대출현황 목록을 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap listCheckOuts(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		List list = bookDAO.listCheckOuts(param);
		int total = bookDAO.getCheckOutsCount(param);

		result.put("flag", "true");
		result.put("total",total);
		result.put("list", list);
		result.put("content", "대출 현황 조회 성공");
		return result;
	}
	
	//============================================================================================
	//연체된 대출정보가 존재하는 사용자들에게 연체 알림 메세지 일괄전송 요청을 처리하는 메소드.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception{
		bookDAO.sendMessage(param);
	}
}