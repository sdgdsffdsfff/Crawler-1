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
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抓取处理
* @ClassName: FetchHanler 
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午1:31:43 
*
 */
public class FetchHandler{
	
	private TaskService taskService;
	
	private static final Logger logger = LoggerFactory.getLogger(FetchHandler.class);
	
	public FetchHandler(){
		taskService = SpringContextHolder.getBean(TaskService.class);
	}
	
	public void handler(Task task){
		try{
			parse(task);
		}catch(SocketTimeoutException e){
			RetryHandler.retryTask(task, taskService);
		}catch(Exception e){
			logger.error("fetch page[{}] error.",task.getUrl(),e);
			RetryHandler.retryTask(taskService);
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
			if(taskService.isFetch(task.getUrl())){
				return ;
			}
			
			//获取cookie
			Cookies cookies = CookieHandler.getCookie(task);
			
			//HttpClient组件请求Http
			HttpHandler httpHandler= SpringContextHolder.getBean(HttpHandler.class);
			CloseableHttpResponse response = httpHandler.execute(task,cookies);
			if(response == null){
				return;
			}
			//获取页面
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity);
			httpHandler.closeResources(response);
			Document doc = Jsoup.parse(html);
			String uri = StringUtils.isBlank(task.getHost()) ? UrlUtils.getHost(doc.baseUri()) : task.getHost();
			
			//保存cookie
			CookieHandler.setCookies(task,httpHandler.getCookieStore().getCookies());
			
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
			task.setStatusCode(httpHandler.getStatusCode());
			taskService.update(task);
			
			/**
			 * 提交保存页面任务
			 */
			//taskExecutor.execute(new TaskHandler(ConvertUtils.toPages(task, doc)));
		}catch(RejectedExecutionException e){
			return;
		}catch(SocketTimeoutException e){
			throw e;
		}catch (UnknownHostException e) {
			throw new SocketTimeoutException(e.getMessage());
		}catch(Exception e){
			throw e;
		}
	}	
}