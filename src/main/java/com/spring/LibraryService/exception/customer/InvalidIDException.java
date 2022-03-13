package com.spring.LibraryService.exception.customer;

public class InvalidIDException extends Exception{
	public InvalidIDException(){
		super("해당 아이디로 가입된 사용자 정보가 존재하지 않습니다.");
	}
	public InvalidIDException(String message){
		super(message);
	}
}