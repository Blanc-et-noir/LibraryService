package com.spring.LibraryService.exception.customer;

public class InvalidPasswordHintAnswerException extends Exception{
	public InvalidPasswordHintAnswerException(){
		super("비밀번호 찾기 질문에 대한 답이 잘못되었습니다.");
	}
	public InvalidPasswordHintAnswerException(String message){
		super(message);
	}
}
