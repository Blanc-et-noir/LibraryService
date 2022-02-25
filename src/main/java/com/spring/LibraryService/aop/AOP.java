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
	
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGONMAV_*(..))")
	private void logonMAV() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGOFFMAV_*(..))")
	private void logoffMAV() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGONMAP_*(..))")
	private void logonMAP() {}
	@Pointcut("execution(* com.spring.LibraryService.controller.*Controller.LOGOFFMAP_*(..))")
	private void logoffMAP() {}
	
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
	
	@Around("logonMAP()")
	private Object logonMAP(ProceedingJoinPoint jp) throws Throwable{
		HashMap<String,String> result = new HashMap<String,String>();		
		if(!isLogon(jp)) {
			result.put("FLAG", "LOGOFF");
			result.put("CONTENT", "로그인 정보가 유효하지 않습니다.");
			return result;
		}else {
			return jp.proceed();		
		}
	}
	
	@Around("logoffMAP()")
	private Object logoffMAP(ProceedingJoinPoint jp) throws Throwable{
		HashMap<String,String> result = new HashMap<String,String>();
		if(isLogon(jp)) {
			result.put("FLAG", "LOGON");
			result.put("CONTENT", "이미 로그인중입니다.");
			return result;
		}else {
			return jp.proceed();		
		}
	}
	
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
	private boolean isLogon(ProceedingJoinPoint jp) {
		CustomerVO customerVO = (CustomerVO) getRequest(jp).getSession().getAttribute("CUSTOMER");
		return customerVO !=null?true:false;
	}
}
