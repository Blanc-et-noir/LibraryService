package com.spring.LibraryService.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.service.MessageService;
import com.spring.LibraryService.vo.CustomerVO;
import com.spring.LibraryService.vo.MessageVO;

@Controller("message")
public class MessageController {
	/*
	
	@Autowired
	private MessageService messageService;

	
	
	
	
	
	//============================================================================================
	//메세지 송신 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/sendMessageForm.do")
	public ModelAndView LOGONMAV_sendMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sendMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 수신 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessageForm.do")
	public ModelAndView LOGONMAV_receiveMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("receiveMessage");
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 읽기 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/readMessage.do")
	public ModelAndView LOGONMAV_readMessageForm(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		info.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
		
		//메세지 읽기에 성공하면, 메세지 상세정보를 보여주는 뷰를 리턴함.
		//해당 mav객체에 읽은 메세지 객체를 담음.
		MessageVO messageVO = messageService.readMessage(info);
		if(messageVO != null) {
			ModelAndView mav = new ModelAndView("readMessage");
			mav.addObject("MESSAGE_BOX", info.get("MESSAGE_BOX"));
			mav.addObject("messageVO", messageVO);
			return mav;
		}else {
			return new ModelAndView("receiveMessage");
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 송신 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/sendMessage.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_sendMessage(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();

		//송신자 ID는 자기 자신의 ID임.
		String SENDER_ID = customerVO.getCUSTOMER_ID();
		info.put("SENDER_ID", SENDER_ID);
		try {
			return messageService.sendMessage(info);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("FLAG","FALSE");
			result.put("CONTENT", "메세지 전송에 실패했습니다.");
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/receiveMessage.do")
	@ResponseBody
	public HashMap LOGONMAP_receiveMessage(@RequestParam HashMap info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		
		//자신과 관련된 메세지만 읽어야 하므로 사용자 ID를 파라미터 맵에 추가함.
		info.put("CUSTOMER_ID",customerVO.getCUSTOMER_ID());
			
		
		//TOTAL은 해당 검색조건에 맞는 메세지들의 수를 나타냄.
		//LIST는 해당 검색조건에 맞는 메세지들의 정보를 리스트에 저장함.
		HashMap temp = messageService.receiveMessage(info);
		result.put("TOTAL", temp.get("TOTAL"));
		result.put("LIST", temp.get("LIST"));
		
		if(((List)temp.get("LIST")).size()!=0) {
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "메세지 조회에 성공했습니다.");
			return result;
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "메세지 정보가 존재하지 않습니다.");
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 삭제 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/message/deleteMessage.do")
	@ResponseBody
	public HashMap LOGONMAP_deleteMessage(@RequestParam HashMap info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		
		//자신의 메세지만을 삭제하기 위해 사용자 ID를 파라미터 맵에 저장함.
		info.put("CUSTOMER_ID",customerVO.getCUSTOMER_ID());
		String temp = request.getParameter("MESSAGE_ID");
		
		//temp변수는 삭제할 메세지들의 ID가 공백 문자로 연결되어있음.
		if(temp.equals("")) {
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "삭제할 메세지 정보가 없습니다.");
			return result;
		}else {
			//메세지 ID를 문자열 배열의 형태로 저장.
			String[] MESSAGE_ID = request.getParameter("MESSAGE_ID").split(" ");
			info.put("LIST", MESSAGE_ID);
			try {
				return messageService.deleteMessage(info);
			}catch(Exception e) {
				result.put("FLAG", "FALSE");
				result.put("CONTENT", "메세지 삭제에 실패했습니다.");
				return result;
			}
		}
	}
	
	
	
	*/
}
