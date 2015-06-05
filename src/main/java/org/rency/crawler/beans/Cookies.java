package org.rency.crawler.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="t_crawler_cookies")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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
		return "{domian:"+domian+", cookieName:"+cookieName+", cookieValue:"+cookieValue+", isSecure:"+isSecure+", version:"+version+", path:"+path+", execDate:"+execDate+"}";
	}
	
}