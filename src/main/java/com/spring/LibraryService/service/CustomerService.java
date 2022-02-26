package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.CustomerDAO;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.vo.CustomerVO;

@Service("customerService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class CustomerService {
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private MailService mailService;
	
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public CustomerVO login(String CUSTOMER_ID, String CUSTOMER_PW) {
		//��Ʈ ���� �̿��Ͽ� ������� ��й�ȣ�� SHA512�� ���� �ؽ��ϰ� �̸� ������.
		//�ܹ��� �ؽ��̹Ƿ� ��ȣȭ�� �Ұ����ϰԲ� ������.
		String salt = customerDAO.getSALT(CUSTOMER_ID);
		if(salt == null) {
			return null;
		}else {
			return customerDAO.login(CUSTOMER_ID, SHA.DSHA512(CUSTOMER_PW,salt));
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String, String> join(CustomerVO customerVO,String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		//ȸ�������Ϸ��� ȸ�� ������ ���� �߰���, ��й�ȣ ã�� ���� ���� ���� ������.
		customerDAO.join(customerVO);
		customerDAO.insertPasswordHint(customerVO, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("FLAG", "TRUE");
		map.put("CONTENT", "ȸ�����Կ� �����߽��ϴ�.");
		return map;
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� �������� ����Ʈ�� ���·� �������� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getPasswordQuestionList() {
		return customerDAO.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//������ ��ȭ��ȣ�� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> findByPhone(String CUSTOMER_PHONE){
		return customerDAO.findByPhone(CUSTOMER_PHONE);
	}
	
	
	
	
	
	
	//============================================================================================
	//������ �̸��Ϸ� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> findByEmail(String CUSTOMER_EMAIL){
		return customerDAO.findByEmail(CUSTOMER_EMAIL);
	}
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ������ �ڽ��� ��й�ȣ ã�� ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> getPasswordQuestion(String CUSTOMER_ID){
		return customerDAO.getPasswordQuestion(CUSTOMER_ID);
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ���� ���� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> validateAnswer(String CUSTOMER_ID, String PASSWORD_HINT_ANSWER){
		//������� ��Ʈ���� �̿��� ������ ���ŵ� ��й�ȣ ã�� ������ ���� ���� SHA512�� ���� �ؽ���.
		//�ؽ̵� ����� DB�� ����� ��й�ȣ ã�� ������ ���� ���� ��ġ�ؾ� ������ ����.
		String SALT = customerDAO.getSALT(CUSTOMER_ID);
		PASSWORD_HINT_ANSWER = SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), SALT);
		HashMap<String,String> map = customerDAO.validateAnswer(CUSTOMER_ID,PASSWORD_HINT_ANSWER);
		
		//������ �����ϸ� ������ ��Ʈ �� 2���� �̿��Ͽ� ���� �ؽ��� ��� 16�ڸ��� �ӽ� ��й�ȣ�ν� �����. 
		String TEMP = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		if(map.get("FLAG").equals("TRUE")) {
			try {
				//�ش� �ӽ� ��й�ȣ�� ������� ��й�ȣ�� �����ϰ�, ��Ʈ���� ���� ������.
				//����ڰ� �����Ҷ� ����� �̸��� �ּҷ� �ӽ� ��й�ȣ�� ������.
				customerDAO.changePassword(CUSTOMER_ID,SHA.DSHA512(TEMP, SALT),SALT);
				mailService.sendMail(map.get("CUSTOMER_EMAIL"), "�ӽ� ��й�ȣ", CUSTOMER_ID+"�� ���� �ӽ� ��й�ȣ�� "+TEMP+" �Դϴ�.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return map; 
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> changePassword(CustomerVO customerVO, String CUSTOMER_PW_OLD, String CUSTOMER_PW, String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		//��Ʈ ���� �� �ش� ��Ʈ ������ ���� ��й�ȣ�� SHA512�� ���� �ؽ��� ����� DB�� ����� ���� �ؽ̵� ��й�ȣ�� ��ġ�ؾ� ��й�ȣ ������. 
		String salt = customerVO.getSALT();
		boolean flag = customerDAO.validatePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW_OLD, salt));
		String newSalt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		if(flag) {
			//��й�ȣ ���� ������ ��й�ȣ�� ��Ʈ ��, ��й�ȣ ã�� ������ ��й�ȣ ã�� ������ ���� ���� ������.
			//�̶� ��й�ȣ�� ��й�ȣ ã�� ������ ���� ���� �ݵ�� �ܹ��� SHA512�� ��Ʈ ������ ���� �ؽ��Ͽ� ������.
			//���Ȼ� ��ȣȭ�� �Ұ����ؾ���.
			customerDAO.changePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW, newSalt), newSalt);
			customerDAO.changePasswordHint(customerVO.getCUSTOMER_ID(), PASSWORD_QUESTION_LIST_ID, SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), newSalt));
			
			//���� �����ÿ� ���� ���ǿ� ����� ����� ��ü�� �����ϱ� ���� �ش� ������ ������.
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "��й�ȣ ���濡 �����߽��ϴ�.");
			map.put("newSalt", newSalt);
			map.put("newCUSTOMER_PW", SHA.DSHA512(CUSTOMER_PW, newSalt));
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "��й�ȣ ���濡 �����߽��ϴ�.");
		}
		return map;
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> changeOther(CustomerVO customerVO, HashMap<String,String> info){
		return customerDAO.changeOther(customerVO,info);
	}
}
