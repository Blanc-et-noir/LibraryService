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
	//�����ڰ� ������ ��û�� �� �ֵ��� ���� ��û ȭ�� �並 �����ϴ� �޼ҵ�.
	//�����ڰ� �ƴ϶�� ����� �� ������, ����� ���������� ������ �䰡 �ƴ϶� ���̵���� �Ϻ� ������� ������.
	//���� ������ ������� �ʴ� �޼ҵ�.
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
	//���� ��Ȳ ������ ��ȸ�� �� �ִ� ȭ�� �並 �����ϴ� �޼ҵ�.
	//�����ڰ� �ƴ϶�� ������ �� ���� ��.
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
	//������Ȳ Ʈ���带 �м��� �� �ִ� ȭ�� �並 �����ϴ� �޼ҵ�.
	//2022.02.26 ���� �̱���.
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
	//���� ���� ��û�� ó���ϴ� �޼ҵ�, �����ڰ� �ƴϸ� ����� �� ����.
	//============================================================================================
	@RequestMapping(value="/book/checkOut.do")
	public ResponseEntity<HashMap> checkOut(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number()!= 0) {
			result.put("flag", "false");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				result = bookService.checkOut(param);
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			}catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "���� ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� ������ ���Ͽ� �ش� ������ �ݳ��ϵ��� ��û�ϴ� �޼ҵ�, �����ڰ� �ƴϸ� ����� �� ����.
	//============================================================================================
	@RequestMapping(value="/book/returnBook.do")
	public ResponseEntity<HashMap> returnBook(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number() != 0) {
			result.put("flag", "false");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				bookService.returnBook(param);
				result.put("flag", "true");
				result.put("content", "�ݳ� ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "�ݳ� ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� �ݳ� ������ �����ϴ� �޼ҵ�, �����ڰ� �ƴϾ �Ϲ� ����ڰ� �ڽ��� ���������� ���Ͽ� �ݳ������� ������ �� �ִ�.
	//============================================================================================
	@RequestMapping(value="/book/renewBook.do")
	public ResponseEntity<HashMap> renewBook(@RequestParam HashMap param,HttpServletRequest request){
		HashMap result = new HashMap();
		try {
			bookService.renewBook(param);
			result.put("flag", "true");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		} catch (Exception e) {
			result.put("flag", "false");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� ��Ȳ ����� ��� �޼ҵ�, �����ڰ� �ƴϸ� ����� �� ����.
	//============================================================================================
	@RequestMapping(value="/book/checkOutList.do")
	public ResponseEntity<HashMap> listCheckOuts(@RequestParam HashMap param, HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");
		
		if(customer.getKind_number() != 0) {
			result.put("flag", "false");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				result = bookService.listCheckOuts(param);
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "���� ��Ȳ ��ȸ ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ڽ��� ������ ������ ���Ͽ� �ݳ����� ��ü ����� �ִ� ����ڵ鿡�� �ϰ������� ��ü �˸� �޼����� �����ϴ� �޼ҵ�.
	//�����ڰ� �ƴϸ� ����� �� ����.
	//============================================================================================
	@RequestMapping(value="/book/sendMessage.do")
	public ResponseEntity<HashMap> sendMessage(@RequestParam HashMap param,HttpServletRequest request){
		HashMap result = new HashMap();
		CustomerVO customer = (CustomerVO) request.getSession().getAttribute("customer");

		if(customer.getKind_number()!= 0) {
			result.put("flag", "false");
			result.put("content", "���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				param.put("customer_id", customer.getCustomer_id());
				bookService.sendMessage(param);
				result.put("flag", "true");
				result.put("content", "���� ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			} catch (Exception e) {
				result.put("flag", "false");
				result.put("content", "���� ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
}
