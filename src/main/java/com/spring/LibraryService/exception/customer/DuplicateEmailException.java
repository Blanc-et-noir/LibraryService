package com.spring.LibraryService.exception.customer;

public class DuplicateEmailException extends Exception{
	public DuplicateEmailException(){
		super("�̹� ������� �̸����Դϴ�.");
	}
	public DuplicateEmailException(String message){
		super(message);
	}
}