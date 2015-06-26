package org.rency.crawler.service.impl;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Task;
import org.rency.crawler.repository.TaskRepository;
import org.rency.crawler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	@Qualifier("taskRepository")
	private TaskRepository taskRepository;

	@Override
	public boolean save(Task task) throws StoreException {
		try{
			if(!isFetch(task.getUrl())){
				return taskRepository.save(task) > 0;
			}
		}catch(DuplicateKeyException e){}
		return false;
	}

	@Override
	public Task get(String url) throws StoreException {
		return taskRepository.get(url);
	}
	
	@Override
	public boolean isFetch(String url) throws StoreException{
		return taskRepository.isFetch(url) > 0;
	}

	@Override
	public Task getTop() throws StoreException {
		return taskRepository.getTop();
	}

	@Override
	public Integer getCount() throws StoreException {
		return taskRepository.getCount();
	}

	@Override
	public Integer getRetryCount(String url) throws StoreException {
		return taskRepository.getRetryCount(url);
	}

	@Override
	public Task getDownloadWithOne() throws StoreException {
		return taskRepository.getDownloadWithOne();
	}

	@Override
	public boolean update(Task task) throws StoreException {
		return taskRepository.update(task) > 0;
	}

	@Override
	public boolean delete(String url) throws StoreException {
		return taskRepository.delete(url) > 0;
	}

	@Override
	public boolean deleteByCrawler(String crawlerId) throws StoreException {
		return taskRepository.deleteByCrawler(crawlerId) > 0;
	}

	@Override
	public boolean deleteAll() throws StoreException {
		return taskRepository.deleteAll() > 0;
	}
}
