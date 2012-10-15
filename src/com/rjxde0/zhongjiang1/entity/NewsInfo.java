package com.rjxde0.zhongjiang1.entity;

public class NewsInfo {
	private String id;
	private String newsTitle;
	private String newsDate;
	private String link;
	
	public NewsInfo() {
		super();
	}
	public NewsInfo(String id, String newsTitle, String newsDate,  String link) {
		super();
		this.id = id;
		this.newsTitle = newsTitle;
		this.newsDate = newsDate;
		this.link = link;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
