package com.rjxde0.zhongjiang1.entity;

public class MoneyProjectInfo {
	private String id;
	private String projectTitle;
	private String projectLink;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectTitle() {
		return projectTitle;
	}
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getProjectLink() {
		return projectLink;
	}
	public void setProjectLink(String projectLink) {
		this.projectLink = projectLink;
	}
	public MoneyProjectInfo() {
		super();
	}
	public MoneyProjectInfo(String id, String projectTitle, String projectLink) {
		super();
		this.id = id;
		this.projectTitle = projectTitle;
		this.projectLink = projectLink;
	}
	
}
