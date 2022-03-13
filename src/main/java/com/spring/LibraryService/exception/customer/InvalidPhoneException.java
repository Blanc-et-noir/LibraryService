package com.spring.LibraryService.exception.customer;

public class InvalidPhoneException extends Exception{
	public InvalidPhoneException(){
		super("해당 전화번호로 가입된 사용자 정보가 존재하지 않습니다.");
	}
	public InvalidPhoneException(String message){
		super(message);
	}
}