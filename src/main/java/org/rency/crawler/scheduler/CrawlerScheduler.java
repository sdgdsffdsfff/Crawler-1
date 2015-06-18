package org.rency.crawler.scheduler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.rency.common.utils.enums.ErrorKind;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.ServerException;
import org.rency.crawler.beans.Configuration;
import org.rency.crawler.beans.Task;
import org.rency.crawler.handler.TaskHandler;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.UrlUtils;
import org.rency.crawler.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * 爬虫调度器
* @ClassName: CrawlerScheduler 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午1:46:48 
*
 */
@Service("crawlerScheduler")
public class CrawlerScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerScheduler.class);
	
	@Autowired
	@Qualifier("taskService")
	private TaskService taskService;
	
	@Autowired
	@Qualifier("crawlerTaskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 启动爬虫
	* @Title: start 
	* @Description: TODO
	* @Date: 2015年6月6日 下午1:48:37
	* @throws CoreException
	 */
	public void start(Configuration cfg) throws CoreException{
		logger.info("启动爬虫[{}]",cfg.getStartAddr());
		ValidateUtils.validateConfiguration(cfg);
		try{
			URL url = new URL(cfg.getStartAddr());
			URLConnection conn = url.openConnection();
			conn.connect();
			Task task = new Task();
			task.setUrl(cfg.getStartAddr());
			task.setHost(UrlUtils.getHost(cfg.getStartAddr()));
			task.setDownload(false);
			task.setHttpMethod(HttpMethod.GET);
			taskService.save(task);
			taskExecutor.execute(new TaskHandler(task));
		}catch(IOException e){
			logger.error("目标网络错误{}",cfg.getStartAddr(),e);
			throw new ServerException(ErrorKind.NET_NOT_FOUND);
		}
		boolean isExec = true;
		while(isExec){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			int activeCount = taskExecutor.getActiveCount();
			if(activeCount == 0){
				taskExecutor.shutdown();
				isExec = false;
				System.out.println("线程池结束");
			}
		}
	}
}
