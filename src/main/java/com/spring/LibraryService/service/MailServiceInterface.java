package com.spring.LibraryService.service;

import java.util.HashMap;

public interface MailServiceInterface {
	//============================================================================================
	//Ư�� �̸��� �ּҷ� ������ �����ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMail(HashMap<String,String> param) throws Exception;
}
