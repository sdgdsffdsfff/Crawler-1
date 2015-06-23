package org.rency.crawler.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.beans.Task;
import org.rency.crawler.service.CookiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

public class CookieHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(CookieHandler.class);
	
	/**
	 * @desc 保存Cookie
	 * @date 2014年12月19日 下午2:48:52
	 * @param task
	 * @param cookies
	 * @throws CoreException
	 */
	public static void setCookies(Task task,List<Cookie> cookiess) throws CoreException{
		if(task.getHttpMethod() != HttpMethod.POST || StringUtils.isBlank(task.getHost()) || cookiess.size() == 0){
			return;
		}
		CookiesService cookiesService = SpringContextHolder.getBean(CookiesService.class);
		for(Cookie cookie : cookiess){
			Cookies cookies = new Cookies();
			cookies.setHost(task.getHost());
			cookies.setDomian(cookie.getDomain());
			cookies.setPath(cookie.getPath());
			cookies.setVersion(cookie.getVersion());
			cookies.setSecure(cookie.isSecure());
			cookies.setCookieName(cookie.getName());
			cookies.setCookieValue(cookie.getValue());
			cookies.setPath(cookie.getPath());
			boolean isSave = cookiesService.add(cookies);
			logger.debug("save cookie[{}] result[{}]",cookies.toString(),isSave);
		}
	}
	
	/**
	 * 获取Cookies
	 * @param task
	 * @return
	 * @throws CoreException
	 */
	public static Cookies getCookie(Task task) throws CoreException{
		if(task.getHttpMethod() == HttpMethod.POST && StringUtils.isNotBlank(task.getHost())){
			Cookies cookies = SpringContextHolder.getBean(CookiesService.class).query(task.getHost());
			logger.debug("get cookie {},host {}",cookies,task.getHost());
			return cookies;
		}
		return null;		
	}
	
}