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
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value="/message/sendMessageForm.do")
	public ModelAndView LOGONMAV_sendMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("sendMessage");
	}
	@RequestMapping(value="/message/receiveMessageForm.do")
	public ModelAndView LOGONMAV_receiveMessageForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("receiveMessage");
	}
	
	@RequestMapping(value="/message/readMessage.do")
	public ModelAndView LOGONMAV_readMessageForm(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		info.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
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
	
	@RequestMapping(value="/message/sendMessage.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_sendMessage(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();

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
	
	@RequestMapping(value="/message/receiveMessage.do")
	@ResponseBody
	public HashMap LOGONMAP_receiveMessage(@RequestParam HashMap info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		
		info.put("CUSTOMER_ID",customerVO.getCUSTOMER_ID());
			
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
	
	@RequestMapping(value="/message/deleteMessage.do")
	@ResponseBody
	public HashMap LOGONMAP_deleteMessage(@RequestParam HashMap info, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		info.put("CUSTOMER_ID",customerVO.getCUSTOMER_ID());
		String temp = request.getParameter("MESSAGE_ID");
		if(temp.equals("")) {
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "삭제할 메세지 정보가 없습니다.");
			return result;
		}else {
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
}
