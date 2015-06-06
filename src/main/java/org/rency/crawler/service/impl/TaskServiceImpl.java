package org.rency.crawler.service.impl;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.repository.TaskRepository;
import org.rency.crawler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	@Qualifier("taskRepository")
	private TaskRepository taskRepository;

	@Override
	public void save(Task task) throws CoreException {
		Task t = get(task.getUrl());
		if(t == null){
			taskRepository.save(task);
		}
	}

	@Override
	public Task get(String url) throws CoreException {
		return taskRepository.get(url);
	}

	@Override
	public Task getTop() throws CoreException {
		return taskRepository.getTop();
	}

	@Override
	public Integer getCount() throws CoreException {
		return taskRepository.getCount();
	}

	@Override
	public Integer getRetryCount(String url) throws CoreException {
		return taskRepository.getRetryCount(url);
	}

	@Override
	public Task getDownloadWithOne() throws CoreException {
		return taskRepository.getDownloadWithOne();
	}

	@Override
	public void update(Task task) throws CoreException {
		taskRepository.update(task);
	}

	@Override
	public void delete(String url) throws CoreException {
		taskRepository.delete(url);
	}

	@Override
	public void deleteByCrawler(String crawlerId) throws CoreException {
		taskRepository.deleteByCrawler(crawlerId);
	}

	@Override
	public void deleteAll() throws CoreException {
		taskRepository.deleteAll();
	}
}
