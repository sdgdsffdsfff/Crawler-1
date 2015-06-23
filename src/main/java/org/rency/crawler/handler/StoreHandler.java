package org.rency.crawler.handler;

import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Pages;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.PagesService;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.utils.CrawlerDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

/**
 * 存储
* @ClassName: StoreHandler 
* @Description: TODO
* @Author user_rcy@163.com
* @Date 2015年6月6日 下午1:31:37 
*
 */
public class StoreHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(StoreHandler.class);
	
	/**
	 * 重试次数
	 */
	private int retryCount = 0;
	
	private TaskService taskService;
	
	private PagesService pagesService;
	
	public StoreHandler(){
		taskService = SpringContextHolder.getBean(TaskService.class);
		pagesService = SpringContextHolder.getBean(PagesService.class);
	}

	/**
	 * 保存页面
	* @Title: store 
	* @Description: TODO
	* @Date: 2015年6月6日 下午5:56:03
	* @param pages
	 */
	public void store(Pages pages) {
		if(pages == null){
			return;
		}
		logger.debug("执行保存任务."+pages.toString());
		try{
			save(pages);
		}catch(StoreException e){
			//睡眠2秒
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			
			if(retryCount < CrawlerDict.RETRY_COUNT){
				retryCount++;
				store(pages);
			}else{
				return;
			}
		}catch(Exception e){
			logger.error("页面保存服务异常.{}",pages,e);
		}
	}
	
	/**
	 * @desc 保存队列任务页面内容 
	 * @date 2014年10月28日 下午3:10:13
	 * @throws Exception
	 */
	private void save(Pages pages) throws StoreException{
		try{
			if(pagesService.save(pages)){
				logger.debug("save page[{}] success",pages.getUrl());
				//更新队列任务状态为已下载
				Task task = new Task();
				task.setUrl(pages.getUrl());
				task.setDownload(true);
				taskService.update(task);
			}else{
				throw new StoreException();
			}
		}catch(DuplicateKeyException e){
			logger.warn("save page[{}] failed, and exists.",pages.getUrl());
		}catch(Exception e) {
			throw new StoreException(e);
		}
	}
	
}