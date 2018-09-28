package com.example.demo;

public class Jobs {

	private String title;

	private String location;

	private String published;

	private String company;

	private String teaser;

	public Jobs() {

	}

	public Jobs(String title, String location, String published, String company) {
		super();
		this.title = title;
		this.location = location;
		this.published = published;
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTeaser() {
		return teaser;
	}

	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

}
