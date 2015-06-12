package org.rency.crawler.repository;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Task;
import org.springframework.stereotype.Repository;

@Repository("taskRepository")
public interface TaskRepository {

	public Integer save(Task task) throws StoreException;
	
	public Task get(String url) throws StoreException;
	
	public Integer isFetch(String url) throws StoreException; 
	
	public Task getTop() throws StoreException;
	
	public Integer getCount() throws StoreException;
	
	public Integer getRetryCount(String url) throws StoreException;
	
	public Task getDownloadWithOne() throws StoreException;
	
	public Integer update(Task task) throws StoreException;
	
	public Integer delete(String url) throws StoreException;
	
	public Integer deleteByCrawler(String crawlerId) throws StoreException;
	
	public Integer deleteAll() throws StoreException;
	
}