package org.rency.crawler.handler;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.CrawlerDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 超时、重试处理
 * @author rencaiyu
 *
 */
public class RetryHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RetryHandler.class);

	/**
	 * 尝试从队列获取新任务重试
	 * @param taskService
	 * @param taskExecutor
	 */
	public static void retryTask(TaskService taskService,ThreadPoolTaskExecutor taskExecutor){
		int retryCount = 0;
		try {
			while(retryCount < CrawlerDict.RETRY_COUNT){
				Task newTask = taskService.getTop();
				if(newTask != null){
					taskExecutor.execute(new TaskHandler(newTask));
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				retryCount++;
			}
			
		} catch (CoreException e) {
			logger.error("尝试抓取任务重试时从队列获取新任务失败，重试次数 {}",retryCount,e);
		}
	}
	
	/**
	 * 抓取网络超时重试
	 * @param taskService
	 * @param taskExecutor
	 */
	public static void retryTask(Task task,TaskService taskService,ThreadPoolTaskExecutor taskExecutor){
		try {
			int retryCount = taskService.getRetryCount(task.getUrl());
			while(retryCount < CrawlerDict.RETRY_COUNT){
				retryCount++;
				task.setRetryCount(retryCount);				
				taskExecutor.execute(new TaskHandler(task));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
			}
			taskService.update(task);
			logger.debug("fetch target address {},retryCount {}",task.toString(),retryCount);
		} catch (CoreException e) {
			logger.error("fetch task {} timeout error.",task.toString(),e);
		}
	}
	
}
