package org.rency.crawler.scheduler;

import java.util.concurrent.RejectedExecutionException;

import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Pages;
import org.rency.crawler.beans.Task;
import org.rency.crawler.handler.FetchHandler;
import org.rency.crawler.handler.StoreHandler;
import org.rency.crawler.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 任务调度器
* @ClassName: TaskThead 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午12:27:52 
*
 */
public class TaskScheduler{
	
	private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
	
	private static final TaskScheduler instance = new TaskScheduler();
	
	/**
	 * 提交抓取任务
	 * @param task
	 * @throws CoreException
	 */
	public static void fetch(Task task) throws CoreException{
		try{
			if(TaskScheduler.save(task)){
				logger.debug("add new fetch task[{}] true.",task.toString());
				ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("fetchTaskExecutor");
				executor.execute(instance.new TaskRunnable(task));
			}
		}catch(RejectedExecutionException e){
			throw e;
		}catch(StoreException e){
			throw e;
		}
	}
	
	/**
	 * 提交存储任务
	 * @param page
	 * @throws CoreException
	 */
	public static void store(Pages page) throws CoreException{
		try{
			ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("storeTaskExecutor");
			executor.execute(instance.new TaskRunnable(page));
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 保存任务
	 * @param task
	 * @return
	 * @throws CoreException
	 */
	public static boolean save(Task task) throws StoreException{
		return SpringContextHolder.getBean(TaskService.class).save(task);
	}
	
	/**
	 * 获取线程池中存活线程个数
	 * @return
	 */
	public static int getActiveCount(){
		ThreadPoolTaskExecutor fetchExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("fetchTaskExecutor");
		//ThreadPoolTaskExecutor storeExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("storeTaskExecutor");
		//return fetchExecutor.getActiveCount() > storeExecutor.getActiveCount() ? storeExecutor.getActiveCount() : fetchExecutor.getActiveCount();
		return fetchExecutor.getActiveCount();
	}
	
	/**
	 * 关闭线程池
	 */
	public static void shutdown(){
		ThreadPoolTaskExecutor fetchExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("fetchTaskExecutor");
		ThreadPoolTaskExecutor storeExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("storeTaskExecutor");
		fetchExecutor.shutdown();
		storeExecutor.shutdown();
		System.out.println("线程池关闭");
	}
	
	private class TaskRunnable implements Runnable{

		private Object targetTask;
		
		public TaskRunnable(Object target){
			this.targetTask = target;
		}
		
		@Override
		public void run() {
			if(targetTask instanceof Task){
				Task task = (Task) targetTask;
				logger.debug("执行抓取任务."+task.toString());
				new FetchHandler().handler(task);
			}else if(targetTask instanceof Pages){
				Pages pages = (Pages) targetTask;
				logger.debug("执行存储任务."+pages.toString());
				new StoreHandler().store(pages);
			}else{
				logger.debug("未知任务类型，拒绝执行."+targetTask);
				return;
			}
		}
	}

}
