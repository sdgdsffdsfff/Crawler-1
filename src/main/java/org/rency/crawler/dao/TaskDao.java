package org.rency.crawler.dao;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.springframework.stereotype.Repository;

@Repository("taskDao")
public interface TaskDao {

	public abstract boolean save(Task task) throws CoreException;
	
	public abstract Task getTaskByUrl(String url) throws CoreException;
	
	public abstract Task getTopTask() throws CoreException;
	
	public abstract Integer getTaskCount() throws CoreException;
	
	public abstract Integer getTaskTimeout(String url) throws CoreException;
	
	public abstract Task getDownloadTaskWithOne() throws CoreException;
	
	public abstract Task getByUrlAndLastModified(String url,String lastModified) throws CoreException;
	
	public abstract boolean updateTask(Task task) throws CoreException;
	
	public abstract boolean deleteByUrl(String url) throws CoreException;
	
	public boolean delete(String crawlerId) throws CoreException;
	
	public abstract boolean deleteAll() throws CoreException;
	
}