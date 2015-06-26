package org.rency.crawler.scheduler;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.StoreException;
import org.rency.common.utils.tool.Utils;
import org.rency.crawler.beans.Pages;
import org.rency.crawler.beans.Task;
import org.rency.crawler.handler.FetchHandler;
import org.rency.crawler.service.PagesService;
import org.rency.crawler.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 任务调度器
* @ClassName: TaskScheduler
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午12:27:52 
*
 */
public class TaskScheduler{
	
	private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
	
	private static final TaskScheduler instance = new TaskScheduler();
	
	private final static ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("crawlerTaskExecutor");
	private final static TaskService taskService = SpringContextHolder.getBean(TaskService.class);
	private final static PagesService pageService = SpringContextHolder.getBean(PagesService.class);
	
	public static AtomicBoolean getTaskLock = new AtomicBoolean(false);
	
	static{
		Thread t = new Thread(instance.new TaskRunnable());
		t.setName("Crawler.Task.Pop");
		t.start();
	}
	
	/**
	 * 提交抓取任务
	 * @param task
	 * @throws CoreException
	 */
	public static void fetch(Task task) throws CoreException{
		try{
			executor.execute(instance.new TaskSchedulerRunnable(task));
		}catch(RejectedExecutionException e){
			logger.error("提交任务线程池拒绝.{}",task.toString());
		}
	}
	
	/**
	 * 保存页面
	 * @param page
	 * @throws CoreException
	 */
	public static void store(Pages page) throws CoreException{
		/*try{
			if(pageService.save(page)){
				logger.debug("save page[{}] success",page.getUrl());
				//更新队列任务状态为已下载
				Task task = new Task("");
				task.setUrl(page.getUrl());
				task.setDownload(true);
				taskService.update(task);
			}else{
				throw new StoreException();
			}
		}catch(DuplicateKeyException e){
			logger.warn("save page[{}] failed, and exists.",page.getUrl());
		}catch(Exception e) {
			throw new StoreException(e);
		}*/
	}
	
	/**
	 * 保存任务
	 * @param task
	 * @return
	 * @throws CoreException
	 */
	public static boolean save(Task task) throws StoreException{
		boolean result = taskService.save(task);
		//触发从队列中获取新任务事件
		getTaskLock.set(true);
		return result;
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
	
	private class TaskSchedulerRunnable implements Runnable{

		private Object targetTask;
		
		public TaskSchedulerRunnable(Object target){
			this.targetTask = target;
		}
		
		@Override
		public void run() {
			if(targetTask instanceof Task){
				Task task = (Task) targetTask;
				logger.debug("执行抓取任务."+task.toString());
				new FetchHandler().handler(task);
			}else{
				logger.debug("未知任务类型，拒绝执行."+targetTask);
				return;
			}
		}
	}

	/**
	 * 从队列中获取新任务，并提交
	 * @author rencaiyu
	 *
	 */
	private class TaskRunnable implements Runnable{

		@Override
		public void run() {
			while(true){
				int counter = 5;
				while(getTaskLock.get()){
					if(counter <= 0){
						getTaskLock.set(false);
					}
					try {
						Task task = taskService.getTop();//任务出栈
						if(task == null){
							Utils.sleep(5);
							continue;
						}
						fetch(task);
					} catch (CoreException e) {
						e.printStackTrace();
					}
					counter--;
				}
			}
		}
		
	}
}
