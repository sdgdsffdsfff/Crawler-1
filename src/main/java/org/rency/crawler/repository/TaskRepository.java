package org.rency.crawler.repository;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.springframework.stereotype.Repository;

@Repository("taskRepository")
public interface TaskRepository {

	public void save(Task task) throws CoreException;
	
	public Task get(String url) throws CoreException;
	
	public Integer isFetch(String url) throws CoreException; 
	
	public Task getTop() throws CoreException;
	
	public Integer getCount() throws CoreException;
	
	public Integer getRetryCount(String url) throws CoreException;
	
	public Task getDownloadWithOne() throws CoreException;
	
	public void update(Task task) throws CoreException;
	
	public void delete(String url) throws CoreException;
	
	public void deleteByCrawler(String crawlerId) throws CoreException;
	
	public void deleteAll() throws CoreException;
	
}