package org.rency.crawler.beans;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.rency.common.utils.tool.DateUtils;
import org.springframework.http.HttpMethod;

/**
 * 抓取队列
* @ClassName: FetchQueue 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午12:36:58 
*
 */
public class Task implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6004790568824598869L;
	
	/**
	 * 抓取地址
	 */
	private String url;
	
	/**
	 * 目标域名
	 */
	private String host;
	
	/**
	 * 爬虫标示
	 */
	private String crawlerId;
	
	/**
	 * 是否已下载
	 */
	private boolean isDownload;
	
	/**
	 * 是否需要抓取
	 */
	private boolean needFetch;
	
	/**
	 * 抓取状态
	 */
	private int statusCode;
	
	/**
	 * 重试次数
	 */
	private int retryCount;
	
	/**
	 * 页面最后修改时间
	 */
	private String lastModified;
	
	/**
	 * Http请求方式
	 */
	private HttpMethod httpMethod;
	
	/**
	 * 访问需要的参数
	 */
	private String params;
	
	private String execDate;
	
	public Task(){
		this.isDownload = false;
		this.needFetch = true;		
		this.httpMethod = HttpMethod.GET;
		this.execDate = DateUtils.getNowDateTimeMills();
	}
	
	public Task(String everyThing){
	}

	public String getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(String crawlerId) {
		this.crawlerId = crawlerId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public boolean isNeedFetch() {
		return needFetch;
	}

	public void setNeedFetch(boolean needFetch) {
		this.needFetch = needFetch;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
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