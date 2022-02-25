package com.spring.LibraryService.vo;

public class CustomerVO {

	private String CUSTOMER_ID;
	private String CUSTOMER_PW;
	private String CUSTOMER_NAME;
	private String CUSTOMER_PHONE;
	private String CUSTOMER_EMAIL;
	private String CUSTOMER_ADDRESS;
	private String CUSTOMER_BDATE;
	private int KIND_NUMBER;
	private String SALT;
	
	public CustomerVO() {
		
	}
	
	public CustomerVO(String CUSTOMER_ID, String CUSTOMER_PW, String CUSTOMER_NAME, String CUSTOMER_PHONE,
			String CUSTOMER_EMAIL, String CUSTOMER_ADDRESS, String CUSTOMER_BDATE, int KIND_NUMBER,String SALT) {
		this.CUSTOMER_ID = CUSTOMER_ID;
		this.CUSTOMER_PW = CUSTOMER_PW;
		this.CUSTOMER_NAME = CUSTOMER_NAME;
		this.CUSTOMER_PHONE = CUSTOMER_PHONE;
		this.CUSTOMER_EMAIL = CUSTOMER_EMAIL;
		this.CUSTOMER_ADDRESS = CUSTOMER_ADDRESS;
		this.CUSTOMER_BDATE = CUSTOMER_BDATE;
		this.KIND_NUMBER = KIND_NUMBER;
		this.SALT = SALT;
	}
	
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		this.CUSTOMER_ID = cUSTOMER_ID;
	}
	public String getCUSTOMER_PW() {
		return CUSTOMER_PW;
	}
	public void setCUSTOMER_PW(String cUSTOMER_PW) {
		this.CUSTOMER_PW = cUSTOMER_PW;
	}
	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}
	public void setCUSTOMER_NAME(String cUSTOMER_NAME) {
		this.CUSTOMER_NAME = cUSTOMER_NAME;
	}
	public String getCUSTOMER_PHONE() {
		return CUSTOMER_PHONE;
	}
	public void setCUSTOMER_PHONE(String cUSTOMER_PHONE) {
		this.CUSTOMER_PHONE = cUSTOMER_PHONE;
	}
	public String getCUSTOMER_EMAIL() {
		return CUSTOMER_EMAIL;
	}
	public void setCUSTOMER_EMAIL(String cUSTOMER_EMAIL) {
		this.CUSTOMER_EMAIL = cUSTOMER_EMAIL;
	}
	public String getCUSTOMER_ADDRESS() {
		return CUSTOMER_ADDRESS;
	}
	public void setCUSTOMER_ADDRESS(String cUSTOMER_ADDRESS) {
		this.CUSTOMER_ADDRESS = cUSTOMER_ADDRESS;
	}
	public String getCUSTOMER_BDATE() {
		return CUSTOMER_BDATE;
	}
	public void setCUSTOMER_BDATE(String cUSTOMER_BDATE) {
		this.CUSTOMER_BDATE = cUSTOMER_BDATE;
	}
	public int getKIND_NUMBER() {
		return KIND_NUMBER;
	}
	public void setKIND_NUMBER(int kIND_NUMBER) {
		this.KIND_NUMBER = kIND_NUMBER;
	}
	public String getSALT() {
		return SALT;
	}
	public void setSALT(String SALT) {
		this.SALT = SALT;
	}
}
