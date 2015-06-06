package org.rency.crawler.handler;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.CookiesService;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.ConvertUtils;
import org.rency.crawler.utils.CrawlerDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

/**
 * 抓取
* @ClassName: FetchHanler 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午1:31:43 
*
 */
public class FetchHanler {
	
	@Autowired
	@Qualifier("cookiesService")
	private CookiesService cookiesService;
	
	@Autowired
	@Qualifier("taskService")
	private TaskService taskService;
	
	@Autowired
	@Qualifier("crawlerTaskExecutor")
	private TaskExecutor taskExecutor;
	
	private static final Logger logger = LoggerFactory.getLogger(FetchHanler.class);
	
	public void taskHandler(Task task){
		try{
			parse(task);
		}catch(SocketTimeoutException e){
			try {
				int timeout = taskService.getRetryCount(task.getUrl());
				if(timeout < CrawlerDict.RETRY_COUNT){
					timeout++;
					task.setRetryCount(timeout);
					taskService.update(task);
					taskHandler(task);
				}
				logger.debug("get target address["+task.getUrl()+"] timeout:"+timeout);
			} catch (CoreException e1) {
				logger.error("query task["+task.getUrl()+"] timeout error."+e);
				e1.printStackTrace();
			}
		}catch(Exception e){
			logger.error("parse page["+task.getUrl()+"] error.",e);
		}
	}
	
	/**
	 * @desc 解析页面
	 * @date 2015年1月8日 下午3:54:31
	 * @throws Exception
	 */
	private void parse(Task task) throws Exception{
		try{
			//判断URL是否已访问过
			if(taskService.get(task.getUrl()) != null){
				return ;
			}
			
			/**
			 * 获取已保存网站cookie
			 */
			Cookies cookies = null;
			if(StringUtils.isNotBlank(task.getHost())){
				cookies = cookiesService.query(task.getHost());
				logger.debug("query cookie["+cookies.toString()+"], and host is "+task.getHost());
			}
			
			//HttpClient组件请求Http
			HttpManager httpManager= SpringContextHolder.getBean(HttpManager.class);
			CloseableHttpResponse response = httpManager.execute(task,cookies);
			if(response == null){
				return;
			}
			//获取页面
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity);
			httpManager.closeResources(response);
			Document doc = Jsoup.parse(html);
			String uri = doc.baseUri();
			
			//保存cookie
			if(cookies ==null){
				setCookies(uri,cookies,httpManager.getCookieStore().getCookies());
			}
			
			/**
			 * 提取页面中Href超链接
			 */
			URLHandler.parseHref(doc);
			
			/**
			 * 提取表单的Action
			 */
			URLHandler.parseForm(doc);
			
			/**
			 * 解析执行Javascript代码
			 */
			URLHandler.parseScript(doc);
			
			//更新队列任务状态
			task.setHost(uri);
			task.setStatusCode(httpManager.getStatusCode());
			taskService.update(task);
			
			/**
			 * 提交保存页面任务
			 */
			taskExecutor.execute(new TaskHandler(ConvertUtils.toPages(task, doc)));
		}catch(RejectedExecutionException e){
			logger.debug("crawler executor service force stoped...",e);
			return;
		}catch(SocketTimeoutException e){
			throw e;
		}catch (UnknownHostException e) {
			logger.debug("Unknown Host."+e+", and give up connection "+task.getUrl());
			throw new SocketTimeoutException(e.getMessage());
		}catch(Exception e){
			logger.error("parse page["+task.getUrl()+"] error.",e);
			throw new CoreException(e);
		}
	}

	/**
	 * @desc 保存Cookie
	 * @date 2014年12月19日 下午2:48:52
	 * @param uri
	 * @param cookieParam
	 * @param cookies
	 * @throws CoreException
	 */
	private void setCookies(String uri,Cookies cookies,List<Cookie> cookiess) throws CoreException{
		if(StringUtils.isNotBlank(uri.trim()) || cookiess.size() != 0){
			for(Cookie cookie : cookiess){
				cookies = new Cookies();
				cookies.setDomian(cookie.getDomain());
				cookies.setPath(cookie.getPath());
				cookies.setVersion(cookie.getVersion());
				cookies.setSecure(cookie.isSecure());
				cookies.setCookieName(cookie.getName());
				cookies.setCookieValue(cookie.getValue());
				cookies.setPath(cookie.getPath());
				boolean isSave = cookiesService.add(cookies);
				logger.debug("save cookie["+cookies.toString()+"] result:"+isSave);
			}
		}
	}
	
}