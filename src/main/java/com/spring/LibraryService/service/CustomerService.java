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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.CustomerDAOInterface;
import com.spring.LibraryService.encrypt.RSA2048;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.vo.CustomerVO;

@Service("customerService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor= {Exception.class})
public class CustomerService implements CustomerServiceInterface{
	@Autowired
	private CustomerDAOInterface customerDAO;
	@Autowired
	private MailService mailService;
	
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception{
		//��Ʈ ���� �̿��Ͽ� ������� ��й�ȣ�� SHA512�� ���� �ؽ��ϰ� �̸� ������.
		//�ܹ��� �ؽ��̹Ƿ� ��ȣȭ�� �Ұ����ϰԲ� ������.
		String privatekey = param.get("privatekey");
		String salt = customerDAO.getSalt(param);
		String customer_pw = param.get("customer_pw");
		
		param.put("customer_pw", SHA.DSHA512(RSA2048.decrypt(customer_pw, privatekey), salt));
		return customerDAO.login(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String, String> join(HashMap<String,String> param) throws Exception{
		HashMap<String,String> result = new HashMap<String,String>();
		//ȸ�������Ϸ��� ȸ�� ������ ���� �߰���, ��й�ȣ ã�� ���� ���� ���� ������.
		customerDAO.join(param);
		customerDAO.insertPasswordHint(param);
		result.put("flag", "true");
		result.put("content", "ȸ�����Կ� �����߽��ϴ�.");
		return result;
		//customerDAO.ERROR();
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
	public ResponseEntity<HashMap> findByPhone(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���̵� ��ȸ ����");
		result.put("password_question", customerDAO.findByPhone(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	

	
	
	
	
	//============================================================================================
	//������ �̸��Ϸ� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> findByEmail(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���̵� ��ȸ ����");
		result.put("password_question", customerDAO.findByEmail(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ������ �ڽ��� ��й�ȣ ã�� ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> getPasswordQuestion(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "���� ��ȸ ����");
		result.put("password_question", customerDAO.getPasswordQuestion(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ���� ���� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> validateAnswer(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		
		//������� ��Ʈ���� �̿��� ������ ���ŵ� ��й�ȣ ã�� ������ ���� ���� SHA512�� ���� �ؽ���.
		//�ؽ̵� ����� DB�� ����� ��й�ȣ ã�� ������ ���� ���� ��ġ�ؾ� ������ ����.
		String salt = customerDAO.getSalt(param);

		param.put("password_hint_answer", SHA.DSHA512(param.get("password_hint_answer").replace(" ", ""), salt));

		//������ �����ϸ� ������ ��Ʈ �� 2���� �̿��Ͽ� ���� �ؽ��� ��� 16�ڸ��� �ӽ� ��й�ȣ�ν� �����. 
		String customer_pw = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		String customer_id = param.get("customer_id");

		customerDAO.validateAnswer(param);
		//�ش� �ӽ� ��й�ȣ�� ������� ��й�ȣ�� �����ϰ�, ��Ʈ���� ���� ������.
		//����ڰ� �����Ҷ� ����� �̸��� �ּҷ� �ӽ� ��й�ȣ�� ������.
		
		param.put("customer_pw", SHA.DSHA512(customer_pw, salt));
		param.put("salt", salt);
		
		customerDAO.changePassword(param);
		
		param.put("to", param.get("customer_email"));
		param.put("subject", "�ӽ� ��й�ȣ");
		param.put("text", customer_id+"�� ���� �ӽ� ��й�ȣ�� "+customer_pw+" �Դϴ�.");
		
		//mailService.sendMail(param);
		
		result.put("flag", "true");
		result.put("content", param.get("customer_email")+"���� �ӽú�й�ȣ�� �����߽��ϴ�.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK); 
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changePassword(HashMap<String,String> param, HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("customer");
		
		//��Ʈ ���� �� �ش� ��Ʈ ������ ���� ��й�ȣ�� SHA512�� ���� �ؽ��� ����� DB�� ����� ���� �ؽ̵� ��й�ȣ�� ��ġ�ؾ� ��й�ȣ ������. 
		String salt = customerVO.getSalt();
		param.put("customer_pw", SHA.DSHA512(param.get("customer_pw_old"), salt));
		customerDAO.validatePassword(param);
		
		String newSalt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		
		//��й�ȣ ���� ������ ��й�ȣ�� ��Ʈ ��, ��й�ȣ ã�� ������ ��й�ȣ ã�� ������ ���� ���� ������.
		//�̶� ��й�ȣ�� ��й�ȣ ã�� ������ ���� ���� �ݵ�� �ܹ��� SHA512�� ��Ʈ ������ ���� �ؽ��Ͽ� ������.
		//���Ȼ� ��ȣȭ�� �Ұ����ؾ���.
		
		param.put("salt", newSalt);
		param.put("customer_pw", SHA.DSHA512(param.get("customer_pw"), newSalt));
		
		customerDAO.changePassword(param);
		
		param.put("password_hint_answer", SHA.DSHA512(param.get("password_hint_answer").replace(" ", ""),newSalt));
		customerDAO.changePasswordHint(param);
		
		session.invalidate();
		
		result.put("flag", "true");
		result.put("content", "��й�ȣ ���濡 �����߽��ϴ�.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changeOther(HashMap<String,String> param, HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		
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
		HttpSession session = request.getSession(true);
		
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
