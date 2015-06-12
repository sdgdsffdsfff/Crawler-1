package org.rency.crawler.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rency.common.utils.exception.ConvertException;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.tool.MD5Utils;
import org.rency.crawler.beans.Pages;
import org.rency.crawler.beans.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

	/**
	 * 将任务对象转换为页面保存对象
	* @Title: toPages 
	* @Description: TODO
	* @Date: 2015年6月6日 下午5:33:11
	* @param task
	* @param doc
	* @return
	* @throws CoreException
	 */
	public static Pages toPages(Task task,Document doc) throws ConvertException{
		try{
			Pages page = new Pages();
			Element head = doc.head();
			Elements metas = head.select("meta[http-equiv]");
			for(Element ele : metas){
				String name = ele.attr("http-equiv");
				String content = ele.attr("content");
				if(name.equals("keywords")){
					page.setKeywords(content);
				}
				if(name.equals("description")){
					page.setDescription(content);
				}
			}
			page.setTitle(doc.title());
			String pageContent = doc.text();
			page.setHtml(doc.html());
			page.setContent(pageContent);
			page.setUrl(task.getUrl());
			page.setSign(MD5Utils.getMD5String(pageContent));
			return page;
		}catch(Exception e){
			logger.warn("队列任务转换对象异常,{},{}",task,doc,e);
			return null;
		}
	}
}