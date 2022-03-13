package com.spring.LibraryService.exception.customer;

public class DuplicatePhoneException extends Exception{
	public DuplicatePhoneException(){
		super("이미 사용중인 전화번호입니다.");
	}
	public DuplicatePhoneException(String message){
		super(message);
	}
}