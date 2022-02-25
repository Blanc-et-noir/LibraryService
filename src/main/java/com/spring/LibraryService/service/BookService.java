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
		
		//1 - �ش� ������� ������Ȳ�� 3�� �̸��ΰ� �˻�
		List list = bookDAO.getCheckOutList(param);
		if(list.size()>=3) {
			result.put("FLAG", "OVERFLOW");
			result.put("CONTENT", "�����ѵ��� �ʰ��Ͽ� ���̻� ������ �Ұ��մϴ�.");
			return result;
		}else {
			//2 - ���� ������ ��ü�� å�� �ִ°� �˻�
			HashMap map;
			for(int i=0; i<list.size(); i++) {
				map = (HashMap) list.get(i);
				double time = Double.parseDouble(String.valueOf(map.get("DIFF")));
				if(time<0) {
					result.put("FLAG", "OVERDUE");
					result.put("CONTENT", "\""+map.get("BOOK_NAME")+"\"������ ���� ��ü������ �����Ͽ� ������ �� �����ϴ�.");
					return result;
				}
			}
			
			//3 - ���Ⱑ�ɽð� ���Ŀ� å�� �����ϴ� ������ �˻�
			map = bookDAO.checkCheckOutDate(param);
			double time = Double.parseDouble(String.valueOf(map.get("DIFF")));
			if(time<0) {
				result.put("FLAG", "OVERDATE");
				result.put("CONTENT", map.get("CHECK_OUT_DATE_STRING")+" ���ĺ��� ������ �����մϴ�.");
				return result;
			}
			
			//å�� ��� 1 ����
			bookDAO.decreaseBookNum(param);
			
			//å������Ȳ�� �ش� �������� �߰�
			bookDAO.insertCheckOut(param);
			
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "å�� �����߽��ϴ�.");
			return result;
		}
	}
	
	public HashMap returnBook(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		//1. �ݳ��ϱ��� ���� SYSDATE�� �ݳ���¥�� ����, ��ü ���� Ȯ��
		HashMap map = bookDAO.isOverdue(param);
		int OVERDUE = Integer.parseInt(String.valueOf(map.get("OVERDUE")+""));
		String CUSTOMER_ID = (String)map.get("CUSTOMER_ID");

		//2. ��ü������ ������� ���Ⱑ�ɽð��� SYSDATE�� �� �ֽ��ǰ� + ��ü�ϼ� ����� ���Ⱑ�ɽð����� ������Ʈ
		if(OVERDUE>0) {
			param.put("OVERDUE", OVERDUE);
			param.put("CUSTOMER_ID", CUSTOMER_ID);
			bookDAO.updateCheckOutDate(param);
		}
		
		//3. �ش� ������ ����� 1 ������Ŵ
		bookDAO.increaseBookNum(param);
		
		//4. ������Ȳ���� �ݳ����θ� Y�� ����
		bookDAO.returnBook(param);

		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "���� �ݳ��� �����߽��ϴ�.");
		return result;
	}
	
	public HashMap renewBook(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		//1. �ش� ���������� ���� Ƚ���� 2ȸ �̸����� �˻�
		if(bookDAO.isExtensible(param)) {			
			//2. �ݳ��Ⱓ�� 7�� ������, ����Ƚ�� 1����
			bookDAO.renewBook(param);
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "����Ⱓ ���忡 �����߽��ϴ�.");
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "����Ⱓ ���� ����Ƚ���� ��� �����Ǿ����ϴ�.");
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
			result.put("CONTENT", "���� ��Ȳ ���� �ҷ����⿡ �����߽��ϴ�.");
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "���� ��Ȳ ���� �ҷ����⿡ �����߽��ϴ�.");
		}
		return result;
	}
	
	public void sendMessage(HashMap param) throws Exception{
		bookDAO.sendMessage(param);
	}
	

}
