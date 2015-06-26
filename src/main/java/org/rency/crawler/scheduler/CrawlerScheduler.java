package org.rency.crawler.scheduler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.enums.ErrorKind;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.ServerException;
import org.rency.crawler.beans.Configuration;
import org.rency.crawler.beans.Task;
import org.rency.crawler.enums.CrawlerStatusKind;
import org.rency.crawler.monitor.CrawlerMonitor;
import org.rency.crawler.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CrawlerScheduler{
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerScheduler.class);
	
	private static Configuration cfg;
	
	/**
	 * 启动爬虫
	 * @param cfg
	 * @throws CoreException
	 */
	public void start(Configuration cfg) throws CoreException{
		CrawlerScheduler.cfg = cfg;
		logger.info("启动爬虫[{}]",cfg.getSeed());
		cfg.validate();
		try{
			URL url = new URL(cfg.getSeed());
			URLConnection conn = url.openConnection();
			conn.connect();
			Task task = new Task();
			task.setUrl(UrlUtils.getFillUrl(cfg.getSeed()));
			task.setHost(UrlUtils.getHost(cfg.getSeed()));
			TaskScheduler.save(task);
		}catch(IOException e){
			logger.error("目标网络错误{}",cfg.getSeed(),e);
			throw new ServerException(ErrorKind.NET_NOT_FOUND);
		}
		boolean isExec = true;
		while(isExec){
			CrawlerStatusKind status = SpringContextHolder.getBean(CrawlerMonitor.class).getStatus();
			if(status == CrawlerStatusKind.STOP){
				TaskScheduler.shutdown();
				isExec = false;
			}
		}
		System.out.println("爬虫抓取完毕");
	}
	
	/**
	 * 停止爬虫
	 * @throws CoreException
	 */
	public void stop() throws CoreException{
		CrawlerStatusKind status = SpringContextHolder.getBean(CrawlerMonitor.class).getStatus();
		if(status != CrawlerStatusKind.STOP){
			TaskScheduler.shutdown();
		}else{
			return;
		}
	}
	
	/**
	 * 获取配置
	 * @return
	 */
	public static Configuration getCfg(){
		return cfg;
	}

	
}
