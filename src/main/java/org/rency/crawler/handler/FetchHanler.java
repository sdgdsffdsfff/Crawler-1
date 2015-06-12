package org.rency.crawler.handler;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 抓取
* @ClassName: FetchHanler 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午1:31:43 
*
 */
public class FetchHanler{
	
	private TaskService taskService;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private static final Logger logger = LoggerFactory.getLogger(FetchHanler.class);
	
	public FetchHanler(){
		taskService = SpringContextHolder.getBean(TaskService.class);
		taskExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("crawlerTaskExecutor");
	}
	
	public void taskHandler(Task task){
		if(task == null){
			return;
		}
		logger.debug("执行抓取任务."+task.toString());
		try{
			parse(task);
		}catch(SocketTimeoutException e){
			RetryHandler.retryTask(task, taskService, taskExecutor);
		}catch(Exception e){
			logger.error("fetch page["+task.getUrl()+"] error.",e);
			RetryHandler.retryTask(taskService, taskExecutor);
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
			if(!taskService.isFetch(task.getUrl())){
				return ;
			}
			
			/**
			 * 获取已保存网站cookie
			 */
			Cookies cookies = null;
			if(StringUtils.isNotBlank(task.getHost())){
				cookies = CookieHandler.getCookie(task.getHost());
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
			String uri = StringUtils.isBlank(task.getHost()) ? doc.baseUri() : task.getHost();
			
			//保存cookie
			if(cookies ==null){
				CookieHandler.setCookies(uri,cookies,httpManager.getCookieStore().getCookies());
			}
			
			/**
			 * 提取页面中Href超链接
			 */
			URLHandler.fetchHref(doc,uri);
			
			/**
			 * 提取表单的Action
			 */
			URLHandler.fetchForm(doc);
			
			/**
			 * 解析执行Javascript代码
			 */
			URLHandler.fetchScript(doc);
			
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
}