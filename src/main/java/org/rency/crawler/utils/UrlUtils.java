package org.rency.crawler.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);

	/**
	 * @description:获取完整的URL路径
	* @author Administrator
	* @date 2015年1月18日 下午2:33:45
	* @param href URL路径
	* @param host 域名
	* @return
	* @throws
	 */
	public static String getFillUrl(String href,String host){
		if(StringUtils.isBlank(href)){
			return "";
		}
		if(StringUtils.isBlank(host)){
			host = UrlUtils.getHost(href);
		}
		if(href.startsWith("javascript:") || href.startsWith("mailto:") || href.startsWith("#") || href.startsWith(host+"#")){
			return "";
		}
		if(href.startsWith("//")){
			href = href.substring(1);
		}
		Matcher matcher = Pattern.compile("((https|http|ftp|rtsp|mms)?://)").matcher(href);
		if(!matcher.find()){
			href = href.indexOf(host) != -1 ? href:host + href;
		}
		if(href.startsWith("/")){
			href = href.indexOf(host) != -1 ? href:host + href;
		}
		return href;
	}
	
	/**
	 * 获取URL域名
	 * @param url
	 * @return
	 */
	public static String getHost(String url){
		try {
			return StringUtils.isBlank(url) ? "":new URL(url).getHost();
		} catch (MalformedURLException e) {
			logger.warn("无法获取其域名{}",url,e.getMessage());
			return "";
		}
	}
	
}