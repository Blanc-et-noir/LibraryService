package com.spring.LibraryService.vo;

import java.sql.Date;

public class ArticleVO {
	public int getARTICLE_ID() {
		return ARTICLE_ID;
	}
	public void setARTICLE_ID(int ARTICLE_ID) {
		this.ARTICLE_ID = ARTICLE_ID;
	}
	public int getPARENT_ARTICLE_ID() {
		return PARENT_ARTICLE_ID;
	}
	public void setPARENT_ARTICLE_ID(int PARENT_ARTICLE_ID) {
		this.PARENT_ARTICLE_ID = PARENT_ARTICLE_ID;
	}
	public int getARTICLE_VIEWS() {
		return ARTICLE_VIEWS;
	}
	public void setARTICLE_VIEWS(int ARTICLE_VIEWS) {
		this.ARTICLE_VIEWS = ARTICLE_VIEWS;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String CUSTOMER_ID) {
		this.CUSTOMER_ID = CUSTOMER_ID;
	}
	public String getARTICLE_TITLE() {
		return ARTICLE_TITLE;
	}
	public void setARTICLE_TITLE(String ARTICLE_TITLE) {
		this.ARTICLE_TITLE = ARTICLE_TITLE;
	}
	public String getARTICLE_CONTENT() {
		return ARTICLE_CONTENT;
	}
	public void setARTICLE_CONTENT(String ARTICLE_CONTENT) {
		this.ARTICLE_CONTENT = ARTICLE_CONTENT;
	}
	public Date getARTICLE_DATE() {
		return ARTICLE_DATE;
	}
	public void setARTICLE_DATE(Date ARTICLE_DATE) {
		this.ARTICLE_DATE = ARTICLE_DATE;
	}
	private int ARTICLE_ID,PARENT_ARTICLE_ID,ARTICLE_VIEWS;
	private String CUSTOMER_ID,ARTICLE_TITLE,ARTICLE_CONTENT;
	private Date ARTICLE_DATE;
}
