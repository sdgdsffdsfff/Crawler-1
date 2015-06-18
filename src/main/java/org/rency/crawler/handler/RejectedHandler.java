package org.rency.crawler.handler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池队列已满，无法继续提交任务处理
 * @author rencaiyu
 *
 */
public class RejectedHandler {

	private AtomicInteger rejectedCount = new AtomicInteger(0);
	
	

	public AtomicInteger getRejectedCount() {
		return rejectedCount;
	}	
	
}
