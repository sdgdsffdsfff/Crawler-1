package org.rency.crawler.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Configuration;
import org.rency.crawler.service.CookiesService;
import org.rency.crawler.service.PagesService;
import org.rency.crawler.service.TaskService;
import org.rency.dal.sequence.Sequence;
import org.rency.dal.sequence.service.SequenceRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CrawlerSchedulerTest {
	
	private CookiesService cookiesService;
	private TaskService taskService;
	private PagesService pagesService;
	private CrawlerScheduler crawlerScheduler;
	private SequenceRepository sequenceRepository;
	
	@Before
	public void before() throws StoreException{
		@SuppressWarnings("resource")
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/applicationContext.xml");
		cookiesService = ctx.getBean(CookiesService.class);
		taskService = ctx.getBean(TaskService.class);
		pagesService = ctx.getBean(PagesService.class);
		crawlerScheduler = ctx.getBean(CrawlerScheduler.class);
		sequenceRepository = ctx.getBean(SequenceRepository.class);
		Map<String, String> createMap = readCreateSQL();
		if(!createMap.isEmpty() || createMap != null){
			for(String tableName : createMap.keySet()){
				sequenceRepository.createTable(tableName, createMap.get(tableName));
			}
		}
		
	}
	
	@Test
	public void testDeleteData() throws CoreException{
		cookiesService.deleteAll();
		taskService.deleteAll();
		pagesService.deleteAll();
	}
	
	@Test
	public void testAddSequence() throws CoreException{
		Sequence sequence= new Sequence();
		sequence.setName("CRAWLER");
		sequence.setCurrentValue(1000000L);
		sequence.setIncrement(1);
		sequence.setTotal(20);
		sequence.setThreshold(5);
		if(sequenceRepository.find(sequence.getName()) == null){
			sequenceRepository.save(sequence);
		}
	}
	
	@Test
	public void testStart() throws CoreException{
		Configuration cfg = new Configuration();
		cfg.setSeed("http://www.itzhai.com/");
		crawlerScheduler.start(cfg);
	}

	private Map<String, String> readCreateSQL(){
		Map<String, String> returnMap = new HashMap<String, String>();
		try{
			File proFile= new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
			String createFile = proFile.getParentFile().getParentFile().getAbsolutePath()+"/database/create.sql";
			FileReader reader = new FileReader(createFile);
			BufferedReader br = new BufferedReader(reader);	
			String sql = "";
			while( (sql = br.readLine()) != null){
				if(sql.startsWith("--")){
					continue;
				}
				System.out.println("读取建表语句:"+sql);
				String tableName = sql.substring(0,sql.indexOf("("));
				tableName = tableName.replace("create table", "").trim();
				returnMap.put(tableName, sql);
			}
			if(br != null){
				br.close();
				reader.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMap;
	}
}
