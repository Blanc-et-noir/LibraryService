package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.service.BookService;
import com.spring.LibraryService.vo.CustomerVO;

@Controller("book")
public class BookController {
	@Autowired
	private BookService bookService;
	
	@RequestMapping(value="/book/checkOutForm.do")
	public ModelAndView LOGONMAV_checkOutForm(HttpServletRequest request, HttpServletResponse response) {
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		if(customerVO.getKIND_NUMBER() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("checkOut");
		}
	}
	
	@RequestMapping(value="/book/listCheckOutForm.do")
	public ModelAndView LOGONMAV_listCheckOutForm(HttpServletRequest request, HttpServletResponse response) {
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		if(customerVO.getKIND_NUMBER() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("listCheckOut");
		}
	}
	
	@RequestMapping(value="/book/analyzeCheckOutForm.do")
	public ModelAndView LOGONMAV_analyzeCheckOutForm(HttpServletRequest request, HttpServletResponse response) {
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		if(customerVO.getKIND_NUMBER() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("analyzeCheckOut");
		}
	}
	
	
	@RequestMapping(value="/book/checkOut.do")
	@ResponseBody
	public HashMap LOGONMAP_checkOut(HttpServletRequest request, HttpServletResponse response){
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		if(customerVO.getKIND_NUMBER() != 0) {
			result.put("FLAG", "NOTADMIN");
			result.put("CONTENT", "해당 사용자는 관리자가 아닙니다.");
		}else {
			HashMap param = new HashMap();
			param.put("BOOK_ISBN", request.getParameter("BOOK_ISBN"));
			param.put("CUSTOMER_ID", request.getParameter("CUSTOMER_ID"));
			try {
				result = bookService.checkOut(param);
			} catch (Exception e) {
				result.put("FLAG", "ERROR");
				result.put("CONTENT", "책을 대출할 수 없습니다.");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/book/returnBook.do")
	@ResponseBody
	public HashMap LOGONMAP_returnBook(HttpServletRequest request, HttpServletResponse response){
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		if(customerVO.getKIND_NUMBER() != 0) {
			result.put("FLAG", "NOTADMIN");
			result.put("CONTENT", "해당 사용자는 관리자가 아닙니다.");
		}else {
			HashMap param = new HashMap();
			param.put("CHECK_OUT_ID", request.getParameter("CHECK_OUT_ID"));
			try {
				result = bookService.returnBook(param);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("FLAG", "ERROR");
				result.put("CONTENT", "해당 정보로는 반납할 수 없습니다.");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/book/renewBook.do")
	@ResponseBody
	public HashMap LOGONMAP_renewBook(HttpServletRequest request, HttpServletResponse response){
		HashMap result = new HashMap();
		HashMap param = new HashMap();
		param.put("CHECK_OUT_ID", request.getParameter("CHECK_OUT_ID"));
		try {
			result = bookService.renewBook(param);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("FLAG", "ERROR");
			result.put("CONTENT", "해당 정보로는 연장할 수 없습니다.");
		}
		return result;
	}
	
	@RequestMapping(value="/book/listCheckOuts.do")
	@ResponseBody
	public HashMap LOGONMAP_listCheckOuts(HttpServletRequest request, HttpServletResponse response){
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		if(customerVO.getKIND_NUMBER() != 0) {
			result.put("FLAG", "NOTADMIN");
			result.put("CONTENT", "해당 사용자는 관리자가 아닙니다.");
		}else {
			HashMap param = new HashMap();
			param.put("ORDERBY", request.getParameter("ORDERBY"));
			param.put("SECTION", request.getParameter("SECTION"));
			param.put("PAGE", request.getParameter("PAGE"));
			param.put("FLAG", request.getParameter("FLAG"));
			param.put("SEARCH", request.getParameter("SEARCH"));
			param.put("START_DATE", request.getParameter("START_DATE"));
			param.put("END_DATE", request.getParameter("END_DATE"));
			try {
				result = bookService.listCheckOuts(param);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("FLAG", "ERROR");
				result.put("CONTENT", "대출 현황 정보가 존재하지 않습니다.");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/book/sendMessage.do")
	@ResponseBody
	public HashMap LOGONMAP_sendMessage(HttpServletRequest request, HttpServletResponse response){
		CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
		HashMap result = new HashMap();
		HashMap param = new HashMap();
		if(customerVO.getKIND_NUMBER() != 0) {
			result.put("FLAG", "NOTADMIN");
			result.put("CONTENT", "해당 사용자는 관리자가 아닙니다.");
		}else {
			try {
				param.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
				bookService.sendMessage(param);
				result.put("FLAG", "TRUE");
				result.put("CONTENT", "연체된 대출정보 메세지 전송에 성공했습니다.");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("FLAG", "ERROR");
				result.put("CONTENT", "연체된 대출정보 메세지 전송에 실패했습니다.");
			}
		}
		return result;
	}
}
