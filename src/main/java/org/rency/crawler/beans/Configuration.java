package org.rency.crawler.beans;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4805885341933940811L;
	
	/**
	 * 爬虫抓取深度
	 */
	private int depth = 2;
	
	/**
	 * 抓取起始地址
	 */
	private String startAddr;
	
	public Configuration(){}
	
	public Configuration(String startAddr){
		this.startAddr = startAddr;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getStartAddr() {
		return startAddr;
	}

	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
