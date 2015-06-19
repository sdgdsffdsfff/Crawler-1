package org.rency.crawler.scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Task;

/**
 * 线程池队列已满，无法继续提交任务处理
 * @author rencaiyu
 *
 */
public class RejectedScheduler {

	private AtomicInteger rejectedCount = new AtomicInteger(0);
	
	private static final int sleep = 1000;
	
	private static ConcurrentHashMap<String,AtomicInteger> fetchQueue = new ConcurrentHashMap<String, AtomicInteger>();
	
	public static void fetchTask(String fetchUrl) throws CoreException{
		if(fetchQueue.containsKey(fetchUrl)){
			fetchQueue.get(fetchUrl).incrementAndGet();
		}
		fetchQueue.put(fetchUrl,new AtomicInteger(0));
	}

	public AtomicInteger getRejectedCount() {
		return rejectedCount;
	}
	
	class TryRejectedTask implements Runnable{

		@Override
		public void run() {
			while(true){
				
			}
		}
		
	}
	
}
