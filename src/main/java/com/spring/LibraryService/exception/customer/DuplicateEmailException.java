package com.spring.LibraryService.exception.customer;

public class DuplicateEmailException extends Exception{
	public DuplicateEmailException(){
		super("이미 사용중인 이메일입니다.");
	}
	public DuplicateEmailException(String message){
		super(message);
	}
}