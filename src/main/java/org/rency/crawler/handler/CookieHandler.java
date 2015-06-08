package org.rency.crawler.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.rency.common.utils.domain.SpringContextHolder;
import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.service.CookiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(CookieHandler.class);
	
	/**
	 * @desc 保存Cookie
	 * @date 2014年12月19日 下午2:48:52
	 * @param uri
	 * @param cookieParam
	 * @param cookies
	 * @throws CoreException
	 */
	public static void setCookies(String uri,Cookies cookies,List<Cookie> cookiess) throws CoreException{
		CookiesService cookiesService = SpringContextHolder.getBean(CookiesService.class);
		if(StringUtils.isNotBlank(uri.trim()) || cookiess.size() != 0){
			for(Cookie cookie : cookiess){
				cookies = new Cookies();
				cookies.setDomian(cookie.getDomain());
				cookies.setPath(cookie.getPath());
				cookies.setVersion(cookie.getVersion());
				cookies.setSecure(cookie.isSecure());
				cookies.setCookieName(cookie.getName());
				cookies.setCookieValue(cookie.getValue());
				cookies.setPath(cookie.getPath());
				boolean isSave = cookiesService.add(cookies);
				logger.debug("save cookie["+cookies.toString()+"] result:"+isSave);
			}
		}
	}
	
	/**
	 * 获取Cookies
	 * @param host
	 * @return
	 * @throws CoreException
	 */
	public static Cookies getCookie(String host) throws CoreException{
		CookiesService cookiesService = SpringContextHolder.getBean(CookiesService.class);
		Cookies cookies = cookiesService.query(host);
		logger.debug("get cookie {},host {}",cookies,host);
		return cookies;
	}
	
}