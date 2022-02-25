package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.BookDAO;

@Service("bookService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class BookService {
	@Autowired
	private BookDAO bookDAO;
	
	public HashMap checkOut(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		//1 - 해당 사용자의 대출현황이 3개 미만인가 검사
		List list = bookDAO.getCheckOutList(param);
		if(list.size()>=3) {
			result.put("FLAG", "OVERFLOW");
			result.put("CONTENT", "대출한도를 초과하여 더이상 대출이 불가합니다.");
			return result;
		}else {
			//2 - 현재 대출중 연체한 책이 있는가 검사
			HashMap map;
			for(int i=0; i<list.size(); i++) {
				map = (HashMap) list.get(i);
				double time = Double.parseDouble(String.valueOf(map.get("DIFF")));
				if(time<0) {
					result.put("FLAG", "OVERDUE");
					result.put("CONTENT", "\""+map.get("BOOK_NAME")+"\"도서에 대한 연체정보가 존재하여 대출할 수 없습니다.");
					return result;
				}
			}
			
			//3 - 대출가능시각 이후에 책을 대출하는 것인지 검사
			map = bookDAO.checkCheckOutDate(param);
			double time = Double.parseDouble(String.valueOf(map.get("DIFF")));
			if(time<0) {
				result.put("FLAG", "OVERDATE");
				result.put("CONTENT", map.get("CHECK_OUT_DATE_STRING")+" 이후부터 대출이 가능합니다.");
				return result;
			}
			
			//책의 재고량 1 감소
			bookDAO.decreaseBookNum(param);
			
			//책대출현황에 해당 대출정보 추가
			bookDAO.insertCheckOut(param);
			
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "책을 대출했습니다.");
			return result;
		}
	}
	
	public HashMap returnBook(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		//1. 반납하기전 먼저 SYSDATE와 반납날짜를 비교함, 연체 여부 확인
		HashMap map = bookDAO.isOverdue(param);
		int OVERDUE = Integer.parseInt(String.valueOf(map.get("OVERDUE")+""));
		String CUSTOMER_ID = (String)map.get("CUSTOMER_ID");

		//2. 연체했으면 사용자의 대출가능시각과 SYSDATE중 더 최신의값 + 연체일수 결과를 대출가능시각으로 업데이트
		if(OVERDUE>0) {
			param.put("OVERDUE", OVERDUE);
			param.put("CUSTOMER_ID", CUSTOMER_ID);
			bookDAO.updateCheckOutDate(param);
		}
		
		//3. 해당 도서의 재고량을 1 증가시킴
		bookDAO.increaseBookNum(param);
		
		//4. 대출현황에서 반납여부를 Y로 설정
		bookDAO.returnBook(param);

		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "도서 반납에 성공했습니다.");
		return result;
	}
	
	public HashMap renewBook(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		//1. 해당 대출정보의 연장 횟수가 2회 미만인지 검사
		if(bookDAO.isExtensible(param)) {			
			//2. 반납기간을 7일 연장함, 연장횟수 1증가
			bookDAO.renewBook(param);
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "대출기간 연장에 성공했습니다.");
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "대출기간 연장 가능횟수가 모두 소진되었습니다.");
		}
		return result;
	}
	
	public HashMap listCheckOuts(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		List list = bookDAO.listCheckOuts(param);
		int TOTAL = bookDAO.getCheckOutsCount(param);
		
		if(list!=null) {
			result.put("FLAG", "TRUE");
			result.put("TOTAL",TOTAL);
			result.put("LIST", list);
			result.put("CONTENT", "대출 현황 정보 불러오기에 성공했습니다.");
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "대출 현황 정보 불러오기에 실패했습니다.");
		}
		return result;
	}
	
	public void sendMessage(HashMap param) throws Exception{
		bookDAO.sendMessage(param);
	}
	

}
