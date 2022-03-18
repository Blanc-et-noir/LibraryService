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

import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.service.MessageServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;
import com.spring.LibraryService.vo.MessageVO;

@Controller("messageController")
public class MessageController {
	@Autowired
	private MessageServiceInterface messageService;

	
	
	
	
	
	//============================================================================================
	//메세지 송신 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/sendMessageForm.do")
	public ModelAndView sendMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sendMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 수신 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessageForm.do")
	public ModelAndView receiveMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("receiveMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 송신 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/readMessageForm.do")
	public ModelAndView readMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("readMessage");
	}
	
	
	
	
	
	//============================================================================================
	//메세지 읽기 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/readMessage.do")
	public ModelAndView readMessageForm(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession(true);
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			param.put("customer_id", customer.getCustomer_id());
			
			//메세지 읽기에 성공하면, 메세지 상세정보를 보여주는 뷰를 리턴함.
			//해당 mav객체에 읽은 메세지 객체를 담음.
		
			MessageVO message = messageService.readMessage(param);		
			
			ModelAndView mav = new ModelAndView("readMessage");
			mav.addObject("message_box", param.get("message_box"));
			mav.addObject("message", message);
			return mav;
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "메세지 조회 실패");
			return new ModelAndView("receiveMessage");
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 송신 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/sendMessage.do")
	public ResponseEntity<HashMap> sendMessage(@RequestParam HashMap<String,String> param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//송신자 ID는 자기 자신의 ID임.
			String sender_id = customer.getCustomer_id();
			param.put("sender_id", sender_id);
			
			messageService.sendMessage(param);
			result.put("flag","true");
			result.put("content", "메세지 전송 성공");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(InvalidIDException e) {
			result.put("flag","false");
			result.put("content", e.getMessage());
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			result.put("flag","false");
			result.put("content", "메세지 전송 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessage.do")
	public ResponseEntity<HashMap> receiveMessage(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//자신과 관련된 메세지만 읽어야 하므로 사용자 ID를 파라미터 맵에 추가함.
			param.put("customer_id",customer.getCustomer_id());
				
			
			//TOTAL은 해당 검색조건에 맞는 메세지들의 수를 나타냄.
			//LIST는 해당 검색조건에 맞는 메세지들의 정보를 리스트에 저장함.
			HashMap temp = messageService.receiveMessage(param);
			
			result.put("total", temp.get("total"));
			result.put("list", temp.get("list"));	
			result.put("flag", "true");
			result.put("content", "메세지 조회 성공");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "메세지 조회 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 삭제 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/deleteAllMessage.do")
	public ResponseEntity<HashMap> deleteAllMessage(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			CustomerVO customer = (CustomerVO) session.getAttribute("customer");
			
			//자신의 메세지만을 삭제하기 위해 사용자 ID를 파라미터 맵에 저장함.
			param.put("customer_id",customer.getCustomer_id());
			
			String[] message_id = ((String)param.get("message_id")).split(" ");
			param.put("list", message_id);
			messageService.deleteMessage(param);
			
			result.put("flag", "true");
			result.put("content", "메세지 삭제 성공");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "메세지 삭제 실패.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}
