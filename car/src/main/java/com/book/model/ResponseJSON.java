package com.book.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseJSON {
	private String status;
	private String message;
	private String url;

	public static ResponseJSON ok(String value){
		
		ResponseJSON resp = new ResponseJSON();
		resp.setStatus("Ok");
		resp.setMessage(value);
		
		return resp;
	}
	
	public static ResponseJSON ok(String value, String url){
		
		ResponseJSON resp = new ResponseJSON();
		resp.setStatus("Ok");
		resp.setMessage(value);
		resp.setUrl(url);
		
		return resp;
	}

	public static ResponseJSON error(String value){
		
		ResponseJSON resp = new ResponseJSON();
		resp.setStatus("ERROR");
		resp.setMessage(value);
		
		return resp;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
