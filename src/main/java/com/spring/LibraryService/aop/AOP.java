package com.spring.LibraryService.aop;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.vo.CustomerVO;

@Aspect
@Component
@Transactional(propagation=Propagation.REQUIRED)
public class AOP {
	
	//============================================================================================
	//��� ��Ʈ�ѷ��� ���Ͽ�, LOGONMAV_, LOGOFFMAV_, LOGONMAP_, LOGOFFMAP_ ���� �����ϴ� �޼ҵ�鿡 AOP����� ����.
	//============================================================================================
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGONMAV_*(..))")
	private void logonMAV() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGOFFMAV_*(..))")
	private void logoffMAV() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGONMAP_*(..))")
	private void logonMAP() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGOFFMAP_*(..))")
	private void logoffMAP() {}
	
	
	
	
	
	
	//============================================================================================
	//�α��� ���¿����ϸ�, �並 �����ϴ� �޼ҵ忡 ������.
	//============================================================================================
	@Around("logonMAV()")
	private Object logonMAV(ProceedingJoinPoint jp){
		if(!isLogon(jp)) {
			return new ModelAndView("login");
		}else {
			try {
				return jp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
				return new ModelAndView("main");
			}			
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�α׾ƿ� ���¿����ϸ�, �並 �����ϴ� �޼ҵ忡 ������.
	//============================================================================================
	@Around("logoffMAV()")
	private Object logoffMAV(ProceedingJoinPoint jp){
		if(isLogon(jp)) {
			return new ModelAndView("main");
		}else {
			try {
				return jp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
				return new ModelAndView("main");
			}			
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�α��� ���¿����ϸ�, JSON �����͸� �����ϴ� �޼ҵ忡 ������.
	//============================================================================================
	@Around("logonMAP()")
	private Object logonMAP(ProceedingJoinPoint jp) throws Throwable{
		HashMap<String,String> result = new HashMap<String,String>();		
		if(!isLogon(jp)) {
			result.put("FLAG", "LOGOFF");
			result.put("CONTENT", "�α��� ������ ��ȿ���� �ʽ��ϴ�.");
			return result;
		}else {
			return jp.proceed();		
		}
	}
	
	
	
	
	
	
	
	//============================================================================================
	//�α׾ƿ� ���¿����ϸ�, JSON �����͸� �����ϴ� �޼ҵ忡 ������.
	//============================================================================================
	@Around("logoffMAP()")
	private Object logoffMAP(ProceedingJoinPoint jp) throws Throwable{
		HashMap<String,String> result = new HashMap<String,String>();
		if(isLogon(jp)) {
			result.put("FLAG", "LOGON");
			result.put("CONTENT", "�̹� �α������Դϴ�.");
			return result;
		}else {
			return jp.proceed();		
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//Request ��ü�� ��� �޼ҵ�.
	//============================================================================================
	private HttpServletRequest getRequest(ProceedingJoinPoint jp) {
		Object[] objects = jp.getArgs();
		HttpServletRequest request = null;
		for(Object obj : objects) {
			if(obj instanceof HttpServletRequest) {
				return (HttpServletRequest)obj;
			}
		}
		return null;
	}
	
	
	
	
	
	
	//============================================================================================
	//Response ��ü�� ��� �޼ҵ�.
	//============================================================================================
	private HttpServletResponse getResponse(ProceedingJoinPoint jp) {
		Object[] objects = jp.getArgs();
		HttpServletResponse request = null;
		for(Object obj : objects) {
			if(obj instanceof HttpServletResponse) {
				return (HttpServletResponse)obj;
			}
		}
		return null;
	}
	
	
	
	
	
	
	//============================================================================================
	//�α��� ���θ� �˻��ϴ� �޼ҵ�.
	//============================================================================================
	private boolean isLogon(ProceedingJoinPoint jp) {
		CustomerVO customerVO = (CustomerVO) getRequest(jp).getSession().getAttribute("CUSTOMER");
		return customerVO !=null?true:false;
	}
}
