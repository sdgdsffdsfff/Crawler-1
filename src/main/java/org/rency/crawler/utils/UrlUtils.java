package org.rency.crawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {

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
		if(StringUtils.isBlank(href) || StringUtils.isBlank(host)){
			return "";
		}
		if(href.startsWith("javascript:") || href.startsWith("mailto:") || href.startsWith("#") || href.startsWith(host+"#")){
			return "";
		}
		Matcher matcher = Pattern.compile("((https|http|ftp|rtsp|mms)?://)").matcher(href);
		if(!matcher.find()){
			href = host + href;
		}
		if(href.startsWith("/")){
			href = host + href;
		}
		return href;
	}
	
}