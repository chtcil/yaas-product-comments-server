package com.codejam.model;

import java.util.Date;

public class Comment {

	public String id;
	public String author;
	public String text;
	public String date;

	public Comment() {
	}

	public Comment(String author, String text) {
		this.author = author;
		this.text = text;
		Date now = new Date();
		this.date = now.toString();
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

}
