package com.spring.LibraryService.service;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.CustomerDAOInterface;
import com.spring.LibraryService.encrypt.RSA2048;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.exception.customer.DuplicateEmailException;
import com.spring.LibraryService.exception.customer.DuplicateIDException;
import com.spring.LibraryService.exception.customer.DuplicatePhoneException;
import com.spring.LibraryService.exception.customer.InvalidEmailException;
import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.exception.customer.InvalidPasswordException;
import com.spring.LibraryService.exception.customer.InvalidPasswordHintAnswerException;
import com.spring.LibraryService.exception.customer.InvalidPhoneException;
import com.spring.LibraryService.vo.CustomerVO;

@Service("customerService")
@Transactional(
	propagation=Propagation.REQUIRED, 
	rollbackFor= {
		Exception.class,
		
		DuplicateIDException.class,
		DuplicatePhoneException.class,
		DuplicateEmailException.class,
		
		InvalidIDException.class,
		InvalidPhoneException.class,
		InvalidEmailException.class,
		InvalidPasswordException.class,
		InvalidPasswordHintAnswerException.class,
		
		MailSendException.class
	}
)
public class CustomerService implements CustomerServiceInterface{
	@Autowired
	private CustomerDAOInterface customerDAO;
	@Autowired
	private MailServiceInterface mailService;
	
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception{
		//��Ʈ ���� �̿��Ͽ� ������� ��й�ȣ�� SHA512�� ���� �ؽ��ϰ� �̸� ������.
		//�ܹ��� �ؽ��̹Ƿ� ��ȣȭ�� �Ұ����ϰԲ� ������.
		String privatekey = param.get("privatekey");
		String customer_salt = customerDAO.getSalt(param);
		String customer_pw = param.get("customer_pw");
		
		param.put("customer_pw", SHA.DSHA512(RSA2048.decrypt(customer_pw, privatekey), customer_salt));
		return customerDAO.login(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String, String> join(HashMap<String,String> param) throws Exception,	DuplicateIDException, DuplicatePhoneException, DuplicateEmailException{
		HashMap<String,String> result = new HashMap<String,String>();
		//ȸ�������Ϸ��� ȸ�� ������ ���� �߰���, ��й�ȣ ã�� ���� ���� ���� ������.
		
		//���̵� �ߺ� Ȯ��
		customerDAO.checkID(param);
		//��ȭ��ȣ �ߺ� Ȯ��
		customerDAO.checkPhone(param);
		//�̸��� �ߺ� Ȯ��
		customerDAO.checkEmail(param);
		
		customerDAO.join(param);
		result.put("flag", "true");
		result.put("content", "ȸ�����Կ� �����߽��ϴ�.");
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� �������� ����Ʈ�� ���·� �������� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getPasswordQuestionList() throws Exception{
		return customerDAO.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//������ ��ȭ��ȣ�� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> findByPhone(HashMap<String,String> param) throws InvalidPhoneException, Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���̵� ��ȸ ���� : "+customerDAO.findByPhone(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	

	
	
	
	
	//============================================================================================
	//������ �̸��Ϸ� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> findByEmail(HashMap<String,String> param) throws InvalidEmailException, Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���̵� ��ȸ ���� : "+customerDAO.findByEmail(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ������ �ڽ��� ��й�ȣ ã�� ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> getPasswordQuestion(HashMap<String,String> param) throws InvalidIDException, Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���� ��ȸ ����");
		result.put("password_question", customerDAO.getPasswordQuestion(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ���� ���� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> validateAnswer(HashMap<String,String> param,HttpServletRequest request) throws InvalidIDException, Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession();
		
		//������� ��Ʈ���� �̿��� ������ ���ŵ� ��й�ȣ ã�� ������ ���� ���� SHA512�� ���� �ؽ���.
		//�ؽ̵� ����� DB�� ����� ��й�ȣ ã�� ������ ���� ���� ��ġ�ؾ� ������ ����.
		String privatekey = (String) session.getAttribute("privatekey");
		String customer_salt = customerDAO.getSalt(param);
		String password_hint_answer = SHA.DSHA512((RSA2048.decrypt(param.get("password_hint_answer"), privatekey)).replaceAll(" ", ""),customer_salt);
		String customer_id = param.get("customer_id");
		
		//������ �����ϸ� ������ ��Ʈ �� 2���� �̿��Ͽ� ���� �ؽ��� ��� 16�ڸ��� �ӽ� ��й�ȣ�ν� �����. 

		param.put("customer_id", customer_id);
		param.put("password_hint_answer", password_hint_answer);
		
		String customer_email = customerDAO.validateAnswer(param);
		String customer_pw = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		
		param.put("customer_pw", SHA.DSHA512(customer_pw, customer_salt));
		
		customerDAO.changePasswordOnly(param);
		
		param.put("to", customer_email);
		param.put("subject", "�ӽ� ��й�ȣ");
		param.put("text", customer_id+"�� ���� �ӽ� ��й�ȣ�� "+customer_pw+" �Դϴ�.");
		mailService.sendMail(param);
		
		result.put("flag", "true");
		result.put("content", customer_email+"���� �ӽú�й�ȣ�� �����߽��ϴ�.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK); 
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changePassword(HashMap<String,String> param, HttpServletRequest request) throws Exception, InvalidPasswordHintAnswerException{
		HashMap result = new HashMap();
		HttpSession session = request.getSession();
		String privatekey = (String)session.getAttribute("privatekey");
		String customer_salt = customerDAO.getSalt(param);		
		String customer_pw_new = param.get("customer_pw");
		//��Ʈ ���� �� �ش� ��Ʈ ������ ���� ��й�ȣ�� SHA512�� ���� �ؽ��� ����� DB�� ����� ���� �ؽ̵� ��й�ȣ�� ��ġ�ؾ� ��й�ȣ ������. 
		
		param.put("customer_pw", SHA.DSHA512(RSA2048.decrypt(param.get("customer_pw_old"), privatekey).replaceAll(" ", ""), customer_salt));
		customerDAO.validatePassword(param);
		
		String newCustomer_salt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		
		//��й�ȣ ���� ������ ��й�ȣ�� ��Ʈ ��, ��й�ȣ ã�� ������ ��й�ȣ ã�� ������ ���� ���� ������.
		//�̶� ��й�ȣ�� ��й�ȣ ã�� ������ ���� ���� �ݵ�� �ܹ��� SHA512�� ��Ʈ ������ ���� �ؽ��Ͽ� ������.
		//���Ȼ� ��ȣȭ�� �Ұ����ؾ���.
		
		param.put("customer_salt", newCustomer_salt);
		param.put("customer_pw", SHA.DSHA512(RSA2048.decrypt(customer_pw_new, privatekey).replaceAll(" ", ""), newCustomer_salt));
		param.put("password_hint_answer", SHA.DSHA512(RSA2048.decrypt(param.get("password_hint_answer"), privatekey).replaceAll(" ", ""),newCustomer_salt));
		customerDAO.changePassword(param);
		
		session.invalidate();
		
		result.put("flag", "true");
		result.put("content", "��й�ȣ ���濡 �����߽��ϴ�.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changeOther(HashMap<String,String> param, HttpServletRequest request) throws Exception, DuplicatePhoneException, DuplicateEmailException{
		HashMap result = new HashMap();
		HttpSession session = request.getSession();
		
		//�̸��� �����ڵ带 �߱޹��� �ʾҰų�, ���� �̸��� ������ ���� ���� ��� �̸��� ������ ��û��.
		if(session.getAttribute("email_authcode") == null || session.getAttribute("email_authflag") == null || session.getAttribute("temp_email") == null){
			
			result.put("flag", "false");
			result.put("content", "�̸��� ������ �Ϸ��ؾ� �մϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			
		//�̸��� ������ ���������� �������� ���� �̸��Ϸ� ������ ���� ������ ��û�� ���, �ٽ� ���ο� �̸��Ͽ� ���� ������ ��û��. 
		}else if(!((String)session.getAttribute("temp_email")).equals(param.get("customer_email"))){
			
			session.removeAttribute("email_authcode");
			result.put("flag", "false");
			result.put("content", "����� �̸��Ͽ� ���� ������ �ٽ��ؾ� �մϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		
		}else {
			CustomerVO customer = (CustomerVO)session.getAttribute("customer");
			param.put("customer_id", customer.getCustomer_id());
			
			customerDAO.checkPhone(param);
			customerDAO.checkEmail(param);
			customerDAO.changeOther(param);
			
			//��Ÿ ���� ���濡 �����ϸ� ������ ������ ������
			session.invalidate();
			
			result.put("flag", "true");
			result.put("content", "ȸ������ ���濡 �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �̸��� �����ڵ带 �����ϰ� �̸��� ������ ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession();
		
		//�̸��� ���� �ڵ带 �߱޹��� �ʾ����� �����ڵ� �߱��� ��û��.
		if(session.getAttribute("email_authcode") == null){
			result.put("flag", "false");
			result.put("content", "�̸��� �����ڵ带 �߱޹޾ƾ� �մϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			
			String email_authcode = (String) session.getAttribute("email_authcode");
			String email_authcode_check = request.getParameter("email_authcode");

			//�Է��� �̸��� �ּҷ� ������ �̸��� ���� �ڵ�� ����ڰ� �Է��� �ڵ尡 ��ġ�� ��쿡�� �̸��� ���� ����.
			if(email_authcode!=null&&email_authcode_check!=null&&email_authcode.equals(email_authcode_check)) {
				result.put("flag", "true");
				result.put("content", "�̸��� ������ �����߽��ϴ�.");
				session.setAttribute("email_authflag", "true");
				session.setAttribute("email_authcode", null);
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			}else {
				result.put("flag", "false");
				result.put("content", "�̸��� ������ �����߽��ϴ�.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ����Ű�� ��û�ϸ� ����Ű�� �߱��ϰ�, �׿� �����Ǵ� ���Ű�� ���ǿ� �����ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> getPublicKey(HttpServletRequest request){
		HashMap result = new HashMap();
		//RSA2048 Ű ��� ��ü ������, ����Ű�� ���Ű�� ��� ���ڿ��� ��ȯ.
		//��ȯ�� ����Ű, ���Ű�� ���ǿ� �����ϰ� ����Ű�� ������.
		try {
			HttpSession session = request.getSession(true);
			
			KeyPair keypair = RSA2048.createKey();
			Key privatekey = keypair.getPrivate();
			Key publickey = keypair.getPublic();
			
			session.setAttribute("privatekey", RSA2048.keyToString(privatekey));
			session.setAttribute("publickey", RSA2048.keyToString(publickey));
			
			result.put("flag", "true");
			result.put("content", "����Ű �߱� ����");
			result.put("publickey", RSA2048.keyToString(publickey));
			return result;
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "����Ű �߱� ����");
			return result;
		}
	}
}
