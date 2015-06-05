package org.rency.crawler.service.impl;

import javax.annotation.Resource;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.dao.TaskDao;
import org.rency.crawler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	@Resource(name="taskDao")
	private TaskDao taskDao;
	
	@Override
	public boolean add(Task task) throws CoreException {
		Task tq = queryTaskByUrl(task.getUrl());
		if(tq == null){
			return taskDao.save(task);
		}else{
			return false;
		}
		
	}

	@Override
	public Task queryTaskByUrl(String url) throws CoreException {
		return taskDao.getTaskByUrl(url);
	}

	@Override
	public Integer queryTaskCount() throws CoreException {
		return taskDao.getTaskCount();
	}

	@Override
	public boolean updateTask(Task task) throws CoreException {
		return taskDao.updateTask(task);
	}
	
	@Override
	public boolean updateTaskTimeout(Task task) throws CoreException {
		return taskDao.updateTask(task);
	}

	@Override
	public boolean updateTaskVisited(Task task) throws CoreException {
		return taskDao.updateTask(task);
	}

	@Override
	public boolean updateTaskDownload(Task task) throws CoreException {
		return taskDao.updateTask(task);
	}

	@Override
	public boolean deleteTask(String url) throws CoreException {
		return taskDao.deleteByUrl(url);
	}

	@Override
	public Task queryTopTask() throws CoreException {
		return taskDao.getTopTask();
	}

	@Override
	public Integer queryTaskTimeout(String url) throws CoreException {
		return taskDao.getTaskTimeout(url);
	}

	@Override
	public boolean deleteAll() throws CoreException {
		return taskDao.deleteAll();
	}

	@Override
	public boolean updateTaskStatusCode(Task task) throws CoreException {
		return taskDao.updateTask(task);
	}
	
	@Override
	public Task queryDownloadTaskWithOne() throws CoreException {
		return taskDao.getDownloadTaskWithOne();
	}
	
	@Override
	public Task queryByLastModifiedAndUrl(String url, String lastModified)throws CoreException {
		return taskDao.getByUrlAndLastModified(url, lastModified);
	}
	
	@Override
	public boolean isVisited(Task task) throws CoreException {
		Task tq = queryTaskByUrl(task.getUrl());
		if((tq != null) && (tq.isVisited()==true) && (tq.getStatusCode() == 200)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteTaskByCrawlerId(String crawlerId) throws CoreException {
		taskDao.delete(crawlerId);
		return true;
	}
}
