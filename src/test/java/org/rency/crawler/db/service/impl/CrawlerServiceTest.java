package org.rency.crawler.db.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.core.CrawlerConfiguration;
import org.rency.crawler.service.CookiesService;
import org.rency.crawler.service.CrawlerService;
import org.rency.crawler.service.TaskService;
import org.rency.crawler.service.WebPageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CrawlerServiceTest {
	
	private CookiesService cookiesService;
	private TaskService taskService;
	private WebPageService webPageService;
	private CrawlerService crawlerService;
	
	@Before
	public void before(){
		@SuppressWarnings("resource")
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/applicationContext.xml");
		cookiesService = ctx.getBean(CookiesService.class);
		taskService = ctx.getBean(TaskService.class);
		webPageService = ctx.getBean(WebPageService.class);
		crawlerService = ctx.getBean(CrawlerService.class);
	}
	
	@Test
	public void testDeleteData() throws CoreException{
		cookiesService.deleteAll();
		taskService.deleteAll();
		webPageService.deleteAll();
	}
	
	@Test
	public void testStart() throws CoreException{		
		CrawlerConfiguration config = new CrawlerConfiguration("123456789", 2, "http://www.csdn.net","987654321");
		//运行爬虫
		crawlerService.start(config);
	}

}
