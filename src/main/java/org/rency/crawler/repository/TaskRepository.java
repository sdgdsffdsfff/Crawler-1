package org.rency.crawler.repository;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Task;
import org.springframework.stereotype.Repository;

@Repository("taskRepository")
public interface TaskRepository {

	/**
	 * 保存任务
	 * @param task
	 * @return
	 * @throws StoreException
	 */
	public Integer save(Task task) throws StoreException;
	
	/**
	 * 获取任务
	 * @param url
	 * @return
	 * @throws StoreException
	 */
	public Task get(String url) throws StoreException;
	
	/**
	 * 加载任务(共享锁控制)
	 * @param url
	 * @return
	 * @throws StoreException
	 */
	public Task load(String url) throws StoreException;
	
	/**
	 * 判断该URL是否已抓过
	 * @param url
	 * @return
	 * @throws StoreException
	 */
	public Integer isFetch(String url) throws StoreException; 
	
	public Task getTop() throws StoreException;
	
	public Integer getCount() throws StoreException;
	
	/**
	 * 获取该任务的重试次数
	 * @param url
	 * @return
	 * @throws StoreException
	 */
	public Integer getRetryCount(String url) throws StoreException;
	
	public Task getDownloadWithOne() throws StoreException;
	
	public Integer update(Task task) throws StoreException;
	
	public Integer delete(String url) throws StoreException;
	
	public Integer deleteByCrawler(String crawlerId) throws StoreException;
	
	public Integer deleteAll() throws StoreException;
	
}