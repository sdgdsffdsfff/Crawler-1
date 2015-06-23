package org.rency.crawler.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.scheduler.TaskScheduler;
import org.rency.crawler.utils.UrlUtils;
import org.springframework.http.HttpMethod;

public class URLHandler {
	
	/**
	 * @desc 提取页面中的超链接
	 * @date 2015年1月8日 下午3:45:33
	 * @param doc 页面
	 * @param host
	 * @throws CoreException
	 */
	public static void fetchHref(Document doc,String host) throws CoreException{
		try{
			Elements hrefs = doc.select("a[href]");
			for(Element ele : hrefs){
				String eleUrl = StringUtils.isBlank(ele.attr("abs:href"))? ele.attr("href").trim() : ele.attr("abs:href").trim();
				String url = UrlUtils.getFillUrl(eleUrl, host);
				if(StringUtils.isBlank(url)){
					continue;
				}
				Task task = new Task();
				task.setUrl(url);
				task.setHost(UrlUtils.getHost(url));
				task.setHttpMethod(HttpMethod.GET);
				TaskScheduler.fetch(task);
			}
		}catch(CoreException e){
			throw e;
		}
	}
	
	/**
	 * @desc 提取表单中的action
	 * @date 2015年1月9日 上午11:05:32
	 * @param doc
	 * @throws CoreException
	 */
	public static void fetchForm(Document doc) throws CoreException{
		try{
			String host = doc.baseUri();
			Elements forms = doc.select("form[action]");
			for(Element form : forms){
				String url = UrlUtils.getFillUrl(form.attr("abs:action").trim(), host);
				if(StringUtils.isBlank(url)){
					continue;
				}
				
				Map<String, String> postParam = new HashMap<String, String>();
				Elements inputs = form.select("input");
				for(Element input : inputs){
					String key = input.attr("name");
					String value = input.attr("value");
					if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
						continue;
					}					
					postParam.put(key, value);
				}
			}
		}catch(Exception e){
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 解析、执行Javascript代码(执行Javascript代码，只需返回URL存入任务队列)
	 * @date 2015年1月8日 下午3:56:59
	 * @param cfg
	 * @param doc
	 * @throws CoreException 
	 */
	public static void fetchScript(Document doc) throws CoreException{
		try{
			Elements scripts = doc.select("script");
			for(Element ele : scripts){
				if(ele.hasAttr("src")){
					String scriptHref = ele.attr("abs:src").trim();
					System.out.println(scriptHref);
				}else{
					String javascript = ele.html();
					System.out.println(javascript);
				}
			}
		}catch(Exception e){
			throw new CoreException(e);
		}
	}
		
}