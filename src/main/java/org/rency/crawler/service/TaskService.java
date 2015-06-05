package org.rency.crawler.service;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Task;

public interface TaskService {

	public boolean add(Task task) throws CoreException;
	
	public Task queryTopTask() throws CoreException;
	
	public Task queryDownloadTaskWithOne() throws CoreException;
	
	public Task queryTaskByUrl(String url) throws CoreException;
	
	public Integer queryTaskCount() throws CoreException;
	
	public Integer queryTaskTimeout(String url) throws CoreException;
	
	public Task queryByLastModifiedAndUrl(String url,String lastModified) throws CoreException;
	
	public boolean updateTask(Task task) throws CoreException;
	
	public boolean updateTaskTimeout(Task task) throws CoreException;
	
	public boolean updateTaskVisited(Task task) throws CoreException;
	
	public boolean updateTaskDownload(Task task) throws CoreException;
	
	public boolean updateTaskStatusCode(Task task)throws CoreException;
	
	public boolean deleteTask(String url) throws CoreException;
	
	public boolean deleteTaskByCrawlerId(String crawlerId) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
	/**
	 * @desc 判断页面是否已访问过
	 * @date 2014年11月3日 下午5:16:46
	 * @param Task
	 * @return
	 * @throws CoreException
	 */
	public boolean isVisited(Task task) throws CoreException;
	
}