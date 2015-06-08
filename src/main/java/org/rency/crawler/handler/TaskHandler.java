package org.rency.crawler.handler;

import org.rency.crawler.beans.Pages;
import org.rency.crawler.beans.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 具体执行任务的线程
* @ClassName: TaskThead 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午12:27:52 
*
 */
public class TaskHandler implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);

	private Object targetTask;
	
	/**
	 * 执行任务
	 * @param task
	 */
	public TaskHandler(Object task){
		this.targetTask = task;
	}
	
	@Override
	public void run() {
		if(targetTask instanceof Task){
			Task task = (Task) targetTask;
			logger.debug("提交抓取任务."+task.toString());
			new FetchHanler().taskHandler(task);
		}else if(targetTask instanceof Pages){
			Pages pages = (Pages) targetTask;
			logger.debug("提交存储任务."+pages.toString());
			new StoreHandler().store(pages);
		}else{
			logger.debug("未知任务类型，拒绝执行."+targetTask);
			return;
		}
	}

}
