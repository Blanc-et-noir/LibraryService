package com.spring.LibraryService.vo;

public class CustomerVO {

	private String customer_id;
	private String customer_pw;
	private String customer_name;
	private String customer_phone;
	private String customer_email;
	private String customer_address;
	private String customer_bdate;
	private int kind_number;
	private String salt;
	
	public CustomerVO() {
		
	}
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_pw() {
		return customer_pw;
	}

	public void setCustomer_pw(String customer_pw) {
		this.customer_pw = customer_pw;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_phone() {
		return customer_phone;
	}

	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}

	public String getCustomer_email() {
		return customer_email;
	}

	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	public String getCustomer_address() {
		return customer_address;
	}

	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}

	public String getCustomer_bdate() {
		return customer_bdate;
	}

	public void setCustomer_bdate(String customer_bdate) {
		this.customer_bdate = customer_bdate;
	}

	public int getKind_number() {
		return kind_number;
	}

	public void setKind_number(int kind_number) {
		this.kind_number = kind_number;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}	
}
