package org.rency.crawler.utils;


public class CrawlerResources {

	/**
	 * 抓取深度
	 */
	private int fetchDepth;
	
	/**
     * 是否抓取重定向的网页
     */
    private boolean fetchFollowRedirects;
	
	/**
	 * 连续发起两次同样的请求的时间间隔(单位秒)
	 */
    private int fetchPolitenessDelay;
	
	/**
	 * 是否抓取Https 加密的网站
	 */
    private boolean fetchIncludeHttpsPages;
    
    /**
     * 页面最大允许大小，超过这个值的页面将不会被保存
     */
    private int fetchMaxDownloadSize;

	public int getFetchDepth() {
		return fetchDepth;
	}

	public void setFetchDepth(int fetchDepth) {
		this.fetchDepth = fetchDepth;
	}

	public boolean isFetchFollowRedirects() {
		return fetchFollowRedirects;
	}

	public void setFetchFollowRedirects(boolean fetchFollowRedirects) {
		this.fetchFollowRedirects = fetchFollowRedirects;
	}

	public int getFetchPolitenessDelay() {
		return fetchPolitenessDelay;
	}

	public void setFetchPolitenessDelay(int fetchPolitenessDelay) {
		this.fetchPolitenessDelay = fetchPolitenessDelay;
	}

	public boolean isFetchIncludeHttpsPages() {
		return fetchIncludeHttpsPages;
	}

	public void setFetchIncludeHttpsPages(boolean fetchIncludeHttpsPages) {
		this.fetchIncludeHttpsPages = fetchIncludeHttpsPages;
	}

	public int getFetchMaxDownloadSize() {
		return fetchMaxDownloadSize;
	}

	public void setFetchMaxDownloadSize(int fetchMaxDownloadSize) {
		this.fetchMaxDownloadSize = fetchMaxDownloadSize;
	}

}