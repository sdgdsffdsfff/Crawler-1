package org.rency.crawler.utils;

import org.rency.crawler.beans.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ValidateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

	/**
	 * 校验爬虫参数
	 * @param cfg
	 */
	public static void validateConfiguration(Configuration cfg){
		logger.info("准备校验启动爬虫参数");
		Assert.notNull(cfg.getStartAddr(),"爬虫抓取地址不能为空");
		logger.info("校验启动爬虫参数完毕");
	}
}
