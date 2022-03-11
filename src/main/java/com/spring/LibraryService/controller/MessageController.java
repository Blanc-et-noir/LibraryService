package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.service.MessageServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;
import com.spring.LibraryService.vo.MessageVO;

@Controller("messageController")
public class MessageController {
	@Autowired
	private MessageServiceInterface messageService;

	
	
	
	
	
	//============================================================================================
	//�޼��� �۽� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/sendMessageForm.do")
	public ModelAndView LOGONMAV_sendMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sendMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ���� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessageForm.do")
	public ModelAndView LOGONMAV_receiveMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("receiveMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �б� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/readMessage.do")
	public ResponseEntity<HashMap> LOGONMAV_readMessageForm(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession(true);
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			param.put("customer_id", customer.getCustomer_id());
			
			//�޼��� �б⿡ �����ϸ�, �޼��� �������� �����ִ� �並 ������.
			//�ش� mav��ü�� ���� �޼��� ��ü�� ����.
			MessageVO message = messageService.readMessage(param);
			
			result.put("message_box", param.get("message_box"));
			result.put("message", message);
			result.put("flag", "true");
			result.put("content", "�޼��� ��ȸ ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "�޼��� ��ȸ ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �۽� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/sendMessage.do")
	public ResponseEntity<HashMap> LOGONMAP_sendMessage(@RequestParam HashMap<String,String> param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//�۽��� ID�� �ڱ� �ڽ��� ID��.
			String sender_id = customer.getCustomer_id();
			param.put("sender_id", sender_id);
			
			messageService.sendMessage(param);
			result.put("flag","true");
			result.put("content", "�޼��� ���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag","false");
			result.put("content", "�޼��� ���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ����� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessage.do")
	public ResponseEntity<HashMap> LOGONMAP_receiveMessage(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//�ڽŰ� ���õ� �޼����� �о�� �ϹǷ� ����� ID�� �Ķ���� �ʿ� �߰���.
			param.put("customer_id",customer.getCustomer_id());
				
			
			//TOTAL�� �ش� �˻����ǿ� �´� �޼������� ���� ��Ÿ��.
			//LIST�� �ش� �˻����ǿ� �´� �޼������� ������ ����Ʈ�� ������.
			HashMap temp = messageService.receiveMessage(param);
			
			result.put("total", temp.get("total"));
			result.put("list", temp.get("list"));	
			result.put("flag", "true");
			result.put("content", "�޼��� ��ȸ ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "�޼��� ��ȸ ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/message/deleteMessage.do")
	public ResponseEntity<HashMap> LOGONMAP_deleteMessage(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//�ڽ��� �޼������� �����ϱ� ���� ����� ID�� �Ķ���� �ʿ� ������.
			param.put("customer_id",customer.getCustomer_id());
			
			String[] message_id = ((String)param.get("message_id")).split(" ");
			param.put("list", message_id);
			messageService.deleteMessage(param);
			
			result.put("flag", "true");
			result.put("content", "�޼��� ���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "�޼��� ���� ����.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}
