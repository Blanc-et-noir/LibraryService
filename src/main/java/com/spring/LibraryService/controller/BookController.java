package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.service.BookServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;

@Controller("bookController")
public class BookController {
	@Autowired
	private BookServiceInterface bookService;
	
	
	
	
	
	//============================================================================================
	//관리자가 대출을 신청할 수 있도록 대출 신청 화면 뷰를 리턴하는 메소드.
	//관리자가 아니라면 사용할 수 없으며, 현재는 독립적으로 구성된 뷰가 아니라 사이드바의 일부 기능으로 합쳐짐.
	//따라서 지금은 사용하지 않는 메소드.
	//============================================================================================
	/*
	@RequestMapping(value="/book/checkOutForm.do")
	public ModelAndView LOGONMAV_checkOutForm(HttpServletRequest request) {
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		if(customer.getKind_number() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("checkOut");
		}
	}
	*/
	
	
	
	
	//============================================================================================
	//대출 현황 정보를 조회할 수 있는 화면 뷰를 리턴하는 메소드.
	//관리자가 아니라면 접근할 수 없는 뷰.
	//============================================================================================
	@RequestMapping(value="/book/checkOutListForm.do")
	public ModelAndView listCheckOutForm(HttpServletRequest request) {
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		if(customer.getKind_number() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("checkOutList");
		}
	}
	
	
	
	
	
	//============================================================================================
	//대출현황 트렌드를 분석할 수 있는 화면 뷰를 리턴하는 메소드.
	//2022.02.26 기준 미구현.
	//============================================================================================
	/*
	@RequestMapping(value="/book/analyzeCheckOutForm.do")
	public ModelAndView LOGONMAV_analyzeCheckOutForm(HttpServletRequest request) {
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		if(customer.getKind_number() != 0) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("analyzeCheckOut");
		}
	}
	*/
	
	
	
	
	//============================================================================================
	//도서 대출 요청을 처리하는 메소드, 관리자가 아니면 사용할 수 없다.
	//============================================================================================
	@RequestMapping(value="/book/checkOut.do")
	public ResponseEntity<HashMap> checkOut(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number()!= 0) {
			result.put("flag", "false");
			result.put("content", "권한 없음");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				result = bookService.checkOut(param);
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			}catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "대출 실패");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//대출 정보에 대하여 해당 도서를 반납하도록 요청하는 메소드, 관리자가 아니면 사용할 수 없다.
	//============================================================================================
	@RequestMapping(value="/book/returnBook.do")
	public ResponseEntity<HashMap> returnBook(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number() != 0) {
			result.put("flag", "false");
			result.put("content", "권한 없음");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				bookService.returnBook(param);
				result.put("flag", "true");
				result.put("content", "반납 성공");
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "반납 실패");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//대출 반납 기한을 연장하는 메소드, 관리자가 아니어도 일반 사용자가 자신의 대출정보에 대하여 반납기한을 연장할 수 있다.
	//============================================================================================
	@RequestMapping(value="/book/renewBook.do")
	public ResponseEntity<HashMap> renewBook(@RequestParam HashMap param,HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			bookService.renewBook(param);
			result.put("flag", "true");
			result.put("content", "연장 성공");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		} catch (Exception e) {
			result.put("flag", "false");
			result.put("content", "연장 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	//============================================================================================
	//대출 현황 목록을 얻는 메소드, 관리자가 아니면 사용할 수 없다.
	//============================================================================================
	@RequestMapping(value="/book/checkOutList.do")
	public ResponseEntity<HashMap> listCheckOuts(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number() != 0) {
			result.put("flag", "false");
			result.put("content", "권한 없음");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				result = bookService.listCheckOuts(param);
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "대출 현황 조회 실패");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//자신이 대출한 도서에 대하여 반납기한 연체 사실이 있는 사용자들에게 일괄적으로 연체 알림 메세지를 전송하는 메소드.
	//관리자가 아니면 사용할 수 없다.
	//============================================================================================
	@RequestMapping(value="/book/sendMessage.do")
	public ResponseEntity<HashMap> sendMessage(@RequestParam HashMap param,HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");

		if(customer.getKind_number()!= 0) {
			result.put("flag", "false");
			result.put("content", "권한 없음");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				param.put("customer_id", customer.getCustomer_id());
				bookService.sendMessage(param);
				result.put("flag", "true");
				result.put("content", "전송 성공");
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "전송 실패");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
}
