package org.rency.crawler.service;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Task;

public interface TaskService {

	public boolean save(Task task) throws StoreException;
	
	public Task get(String url) throws StoreException;
	
	public Task load(String url) throws StoreException;
	
	public boolean isFetch(String url) throws StoreException; 
	
	public Task getTop() throws StoreException;
	
	public Integer getCount() throws StoreException;
	
	public Integer getRetryCount(String url) throws StoreException;
	
	public Task getDownloadWithOne() throws StoreException;
	
	public boolean update(Task task) throws StoreException;
	
	public boolean delete(String url) throws StoreException;
	
	public boolean deleteByCrawler(String crawlerId) throws StoreException;
	
	public boolean deleteAll() throws StoreException;
}