package org.rency.crawler.beans;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Cookies implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2116620442965932043L;

	private String domian;
	private String cookieName;
	private String cookieValue;
	private boolean isSecure;
	private int version;
	private String path;
	private Date execDate;
	
	public Cookies(){
		this.execDate = new Date();
	}
	
	public Cookies(String domian,String cookieName,String cookieValue,boolean isSecure,int version,String path){
		this.domian = domian;
		this.cookieName = cookieName;
		this.cookieValue = cookieValue;
		this.isSecure = isSecure;
		this.version = version;
		this.path = path;
	}

	public String getDomian() {
		return domian;
	}

	public void setDomian(String domian) {
		this.domian = domian;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getCookieValue() {
		return cookieValue;
	}

	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	public boolean isSecure() {
		return isSecure;
	}

	public void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}