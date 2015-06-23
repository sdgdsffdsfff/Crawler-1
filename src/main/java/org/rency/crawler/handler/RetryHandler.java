package org.rency.crawler.handler;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.scheduler.TaskScheduler;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.CrawlerDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 超时、重试处理
 * @author rencaiyu
 *
 */
public class RetryHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RetryHandler.class);

	/**
	 * 从队列获取新任务
	 * @param taskService
	 */
	public static void retryTask(TaskService taskService){
		int retryCount = 0;
		try {
			while(retryCount < CrawlerDict.RETRY_COUNT){
				Task newTask = taskService.getTop();
				if(newTask != null){
					TaskScheduler.fetch(newTask);
				}else{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}
				retryCount++;
			}			
		} catch (CoreException e) {
			logger.error("尝试从队列获取新任务失败，重试次数 {}",retryCount,e);
		}
	}
	
	/**
	 * 访问超时
	 * @param taskService
	 */
	public static void retryTask(Task task,TaskService taskService){
		try {
			int retryCount = taskService.getRetryCount(task.getUrl());
			while(retryCount < CrawlerDict.RETRY_COUNT){
				retryCount++;
				task.setRetryCount(retryCount);				
				TaskScheduler.fetch(task);
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
