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
	//�Է¹��� ������ ���� ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap checkOut(HashMap param) throws InvalidBookISBNException,RunOutOfBookNumberException, Exception{
		HashMap result = new HashMap();
		//�ش� ������ �ִ��� �˻�
		bookDAO.checkBookISBN(param);
		
		//�ش� ���� ��� ������ �ִ��� �˻�
		bookDAO.checkBookNumber(param);
		
		//�ش� ������� ������Ȳ�� 3�� �̸��ΰ� �˻�.
		List list = bookDAO.getCheckOutList(param);
		if(list.size()>=3) {
			result.put("flag", "false");
			result.put("content", "�����ѵ� �ʰ�");
			return result;
		}else {
			//���� ������ ��ü�� å�� �ִ°� �˻�.
			HashMap map;
			for(int i=0; i<list.size(); i++) {
				map = (HashMap) list.get(i);
				double time = Double.parseDouble(String.valueOf(map.get("diff")));
				if(time<0) {
					result.put("flag", "false");
					result.put("content", "\""+map.get("book_name")+"\" ���� ��ü");
					return result;
				}
			}
			
			//���Ⱑ�ɽð� ���Ŀ� å�� �����ϴ� ������ �˻�.
			map = bookDAO.checkCheckOutDate(param);
			double time = Double.parseDouble(String.valueOf(map.get("diff")));
			
			if(time<0) {
				result.put("flag", "false");
				result.put("content", map.get("check_out_date_string")+" ���ĺ��� ������ ����");
				return result;
			}
			
			//å�� ��� 1 ���ҽ�Ŵ.
			bookDAO.decreaseBookNum(param);
			
			//å������Ȳ�� �ش� �������� �߰�.
			bookDAO.insertCheckOut(param);
			
			result.put("flag", "true");
			result.put("content", "���� ����");
			return result;
		}
	}

	//============================================================================================
	//������ ������ ���� �ݳ��� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void returnBook(HashMap param) throws InvalidBookISBNException, InvalidCheckOutIDException, Exception{
		//�ݳ��ϱ��� ���� ���糯¥�� �ݳ���¥�� ����, ��ü ���� Ȯ��.
		HashMap map = bookDAO.isOverdue(param);
		int overdue = Integer.parseInt(String.valueOf(map.get("overdue")+""));
		String customer_id = (String)map.get("customer_id");

		//�ش� ������ �ִ��� �˻�
		bookDAO.checkBookISBN(param);

		//�ش� ���������� �ִ��� �˻�
		bookDAO.checkCheckOutID(param);
		
		//��ü������ ������� ���Ⱑ�ɽð��� ���糯¥�� �� �ֽ��ǰ� + ��ü�ϼ� ����� ���Ⱑ�ɽð����� ������Ʈ.
		if(overdue>0) {
			param.put("overdue", overdue);
			param.put("customer_id", customer_id);
			bookDAO.updateCheckOutDate(param);
		}
		
		//�ش� ������ ����� 1 ������Ŵ.
		bookDAO.increaseBookNum(param);
		
		//������Ȳ���� �ݳ����θ� Y�� ����.
		bookDAO.returnBook(param);
	}
	
	//============================================================================================
	//���� ���� �ݳ����� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void renewBook(HashMap param, HttpServletRequest request) throws InvalidBookISBNException, ExhaustedRenewCountException, Exception{
		//�ش� ������ �ִ��� �˻�
		bookDAO.checkBookISBN(param);
		
		//���� Ƚ���� �����ִ��� �˻�
		bookDAO.checkRenewCount(param);
		
		CustomerVO customer = (CustomerVO)(request.getSession().getAttribute("customer"));
		if(customer.getKind_number()==0) {
			//�����ڰ� ������ ��û
			bookDAO.renewBookAsAdmin(param);
		}else {
			//�����ڰ� ������ ��û
			param.put("customer_id", customer.getCustomer_id());
			bookDAO.renewBookAsCustomer(param);
		}
	}

	//============================================================================================
	//������Ȳ ����� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap listCheckOuts(HashMap param) throws Exception{
		HashMap result = new HashMap();
		
		List list = bookDAO.listCheckOuts(param);
		int total = bookDAO.getCheckOutsCount(param);

		result.put("flag", "true");
		result.put("total",total);
		result.put("list", list);
		result.put("content", "���� ��Ȳ ��ȸ ����");
		return result;
	}
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception{
		bookDAO.sendMessage(param);
	}
}