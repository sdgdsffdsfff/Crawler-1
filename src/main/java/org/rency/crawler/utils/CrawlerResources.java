package org.rency.crawler.utils;

import org.springframework.beans.factory.annotation.Value;

public class CrawlerResources {

	@Value("${crawler.fetch.depth}")
	public static int crawlerFetchDepth;

}