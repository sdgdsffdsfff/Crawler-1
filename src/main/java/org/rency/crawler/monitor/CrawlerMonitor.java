package org.rency.crawler.monitor;

import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.tool.Utils;
import org.rency.crawler.enums.CrawlerStatusKind;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 爬虫监视器
 * @author rencaiyu
 *
 */
public class CrawlerMonitor implements InitializingBean{

	private ThreadPoolTaskExecutor crawlerExecutor = (ThreadPoolTaskExecutor) SpringContextHolder.getBean("crawlerTaskExecutor");
	
	/**
	 * 爬虫状态
	 */
	private volatile CrawlerStatusKind status;
	
	/**
	 * 获取爬虫状态
	 * @return
	 */
	public CrawlerStatusKind getStatus(){
		return status;
	}
	
	public void setStatus(CrawlerStatusKind status) {
		this.status = status;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		crawlerExecutor.setThreadGroupName("Crawler.Thread.Pool");
		this.status = CrawlerStatusKind.INIT;
		Thread t = new Thread(new StatusRunnable());
		t.setName("Crawler.Monitor.Status");
		t.start();
	}
	
	/**
	 * 爬虫状态监测
	 * @author rencaiyu
	 *
	 */
	private class StatusRunnable implements Runnable{		
		
		@Override
		public void run() {
			while(true){
				//当状态为INIT时，如果活动线程数量不等于0，则表示线程池处于运行状态
				if(status == CrawlerStatusKind.INIT || status == CrawlerStatusKind.START){
					int activeCount = crawlerExecutor.getActiveCount();
					if(activeCount != 0){
						status = CrawlerStatusKind.RUNNING;
					}
				}
				//当状态为RUNNING时，如果活动线程数量等于0，则表示线程池处于空闲状态
				if(status == CrawlerStatusKind.RUNNING){
					boolean isExit = false;
					int exitCount = 3;
					while(!isExit){
						int activeCount = crawlerExecutor.getActiveCount();
						if(activeCount == 0 && exitCount == 0){
							status = CrawlerStatusKind.STOP;
							isExit = true;
						}
						Utils.sleep(2);
						exitCount--;
					}
				}
				Utils.sleep(5);
			}
		}
	}
}