package org.rency.crawler.beans;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.rency.common.utils.tool.DateUtils;

public class Pages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 页面地址
	 */
	private String url;
	/**
	 * 页面内容
	 */
	private String html;
	
	/**
	 * 页面文本内容
	 */
	private String content;
	/**
	 * 页面签名
	 */
	private String sign;
	/**
	 * 字符集
	 */
	private String charset;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 关键字
	 */
	private String keywords;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 是否已创建索引
	 */
	private boolean isCreateIndex;
	private String execDate;
	
	public Pages(){
		this.isCreateIndex = false;
		this.execDate = DateUtils.getNowDateTimeMills();
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getCharset() {
		return charset;
	}


	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getKeywords() {
		return keywords;
	}


	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public boolean isCreateIndex() {
		return isCreateIndex;
	}


	public void setCreateIndex(boolean isCreateIndex) {
		this.isCreateIndex = isCreateIndex;
	}


	public String getExecDate() {
		return execDate;
	}


	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
