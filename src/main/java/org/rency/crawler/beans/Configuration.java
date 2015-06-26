package org.rency.crawler.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.crawler.utils.CrawlerDict;
import org.rency.crawler.utils.CrawlerResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4805885341933940811L;
	
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
		
	/**
	 * 抓取起始种子
	 */
	private String seed;
	
	/**
	 * 抓取深度
	 */
	private int depth;
	
	private List<String> fetchAreas;
	
	public Configuration(){
		this.depth = SpringContextHolder.getBean(CrawlerResources.class).getFetchDepth();
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public List<String> getFetchAreas() {
		return fetchAreas;
	}

	public void setFetchAreas(List<String> fetchAreas) {
		this.fetchAreas = fetchAreas;
	}

	public void validate(){
		logger.info("---------------准备校验启动爬虫参数------------------");
		logger.info("抓取策略:"+CrawlerDict.CRAWLER_FETCH_POLICY);
		Assert.notNull(this.getSeed(),"抓取种子不能为空.");
		logger.info("起始种子[{}].",this.getSeed());
		Assert.notNull(this.getDepth(),"抓取深度不能为空.");
		logger.info("抓取深度[{}].",this.getDepth());
		if(fetchAreas == null || fetchAreas.size() == 0){
			logger.info("抓取页面范围[默认抓取页面所有区域中的超链接].");
		}
		logger.info("---------------校验启动爬虫参数完毕------------------");
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
