package com.spring.LibraryService.vo;

import java.sql.Date;

public class MessageVO {
	
	private String SENDER_ID, RECEIVER_ID, MESSAGE_TITLE, MESSAGE_CONTENT,MESSAGE_DATE_STRING;
	private int MESSAGE_ID;
	private Date MESSAGE_DATE;
	
	public MessageVO(int MESSAGE_ID, String SENDER_ID, String RECEIVER_ID, Date MESSAGE_TIME, String MESSAGE_TITLE,
			String MESSAGE_CONTENT) {
		this.MESSAGE_ID = MESSAGE_ID;
		this.SENDER_ID = SENDER_ID;
		this.RECEIVER_ID = RECEIVER_ID;
		this.MESSAGE_DATE = MESSAGE_TIME;
		this.MESSAGE_TITLE = MESSAGE_TITLE;
		this.MESSAGE_CONTENT = MESSAGE_CONTENT;
	}
	public MessageVO() {
		
	}
	public int getMESSAGE_ID() {
		return MESSAGE_ID;
	}

	public void setMESSAGE_ID(int MESSAGE_ID) {
		this.MESSAGE_ID = MESSAGE_ID;
	}

	public String getSENDER_ID() {
		return this.SENDER_ID;
	}

	public void setSENDER_ID(String SENDER_ID) {
		this.SENDER_ID = SENDER_ID;
	}

	public String getRECEIVER_ID() {
		return RECEIVER_ID;
	}

	public void setRECEIVER_ID(String RECEIVER_ID) {
		this.RECEIVER_ID = RECEIVER_ID;
	}
	public String getMESSAGE_DATE_STRING() {
		return MESSAGE_DATE_STRING;
	}

	public void setMESSAGE_DATE_STRING(String MESSAGE_DATE_STRING) {
		this.MESSAGE_DATE_STRING = MESSAGE_DATE_STRING;
	}
	public Date getMESSAGE_DATE() {
		return MESSAGE_DATE;
	}

	public void setMESSAGE_DATE(Date MESSAGE_DATE) {
		this.MESSAGE_DATE = MESSAGE_DATE;
	}

	public String getMESSAGE_TITLE() {
		return MESSAGE_TITLE;
	}

	public void setMESSAGE_TITLE(String MESSAGE_TITLE) {
		this.MESSAGE_TITLE = MESSAGE_TITLE;
	}

	public String getMESSAGE_CONTENT() {
		return MESSAGE_CONTENT;
	}

	public void setMESSAGE_CONTENT(String MESSAGE_CONTENT) {
		this.MESSAGE_CONTENT = MESSAGE_CONTENT;
	}	
}

