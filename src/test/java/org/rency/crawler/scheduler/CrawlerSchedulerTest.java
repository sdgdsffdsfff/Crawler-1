package org.rency.crawler.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.service.CookiesService;
import org.rency.crawler.service.PagesService;
import org.rency.crawler.service.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CrawlerSchedulerTest {
	
	private CookiesService cookiesService;
	private TaskService taskService;
	private PagesService pagesService;
	private CrawlerScheduler crawlerScheduler;
	
	@Before
	public void before(){
		@SuppressWarnings("resource")
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/applicationContext.xml");
		cookiesService = ctx.getBean(CookiesService.class);
		taskService = ctx.getBean(TaskService.class);
		pagesService = ctx.getBean(PagesService.class);
		crawlerScheduler = ctx.getBean(CrawlerScheduler.class);
	}
	
	@Test
	public void testDeleteData() throws CoreException{
		cookiesService.deleteAll();
		taskService.deleteAll();
		pagesService.deleteAll();
	}
	
	@Test
	public void testStart() throws CoreException{
		crawlerScheduler.start("http://www.csdn.net");
	}

}
