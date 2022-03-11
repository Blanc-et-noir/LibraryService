package com.spring.LibraryService.service;

import java.util.HashMap;

public interface MailServiceInterface {
	//============================================================================================
	//특정 이메일 주소로 메일을 전송하는 메소드.
	//============================================================================================
	public void sendMail(HashMap<String,String> param) throws Exception;
}
