package org.rency.crawler.handler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.rency.common.utils.exception.CoreException;
import org.rency.common.utils.exception.NotModifiedException;
import org.rency.common.utils.tool.ConvertUtils;
import org.rency.common.utils.tool.HttpUtils;
import org.rency.common.utils.tool.XmlUtils;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.beans.Task;
import org.rency.crawler.utils.CrawlerDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

public class HttpHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
	
	private String userAgentXmlPath;
	
	/**
     * Socket超时时间
     */
    private int socketTimeout;
    
    /**
     * 连接超时时间设置
     */
    private int connectionTimeout;
    
	private final int maxTotal = 100;
        
    private HttpClientContext context = HttpClientContext.create();
    
    private PoolingHttpClientConnectionManager cm;
    
    private CookieStore cookieStore;
    
    private CloseableHttpClient httpClient;
    
    private RequestConfig requestConfig;
    
    private int statusCode;
    
    public HttpHandler(){
    	if(cookieStore == null){
    		cookieStore = new BasicCookieStore();
    	}
    	
    	if(cm == null){
    		cm = new PoolingHttpClientConnectionManager();
    		cm.setMaxTotal(maxTotal);
    	}
    	
    	if(httpClient == null){
    		httpClient = HttpClients.custom()
    				.setConnectionManager(cm)
    				.setRedirectStrategy(new LaxRedirectStrategy())
    				.setDefaultCookieStore(cookieStore)
    				.setRetryHandler(new HttpRequestRetryHandler() {
						@Override
						public boolean retryRequest(IOException exception, int executionCount,HttpContext context) {
							if(executionCount >= CrawlerDict.RETRY_COUNT){
								return false;
							}
							if(exception instanceof NoHttpResponseException){
								return true;
							}
							if(exception instanceof ConnectionPoolTimeoutException){
								return true;
							}
							if(exception instanceof SocketTimeoutException){
								return true;
							}
							if(exception instanceof SSLHandshakeException){
								return false;
							}
							HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
							boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
							if(idempotent){
								return true;
							}
							return false;
						}
					})
    				.build();
    	}    	
    	
    	if(requestConfig == null){
    		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectionTimeout).setConnectionRequestTimeout(connectionTimeout).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();
    	}
    	
    }
    
    /**
     * @desc 发送http请求
     * @date 2015年1月9日 下午3:59:17
     * @param task
     * @param cookies
     * @return
     * @throws Exception
     */
    public CloseableHttpResponse execute(Task task,Cookies cookies)throws Exception{
    	try{
			//设置cookie
			setCookie(cookies);
			
			if(task.getHttpMethod().name().equals(HttpMethod.GET.name())){
				return get(task);
			}else{
				return post(task);
			}
		}catch(Exception e){
			throw new CoreException(e);
		}
    }
    
    /**
     * @desc 以get方式发送请求
     * @date 2015年1月9日 下午2:34:34
     * @param task
     * @return
     * @throws Exception
     */
	private CloseableHttpResponse get(Task task) throws Exception{
		try{
			HttpGet httpget = new HttpGet(task.getUrl());
			logger.debug("executing url[{}] start.",task.getUrl());
	        httpget.setConfig(requestConfig);
	        httpget.setHeader(CrawlerDict.USER_AGENT, getUserAgent());
			CloseableHttpResponse response = httpClient.execute(httpget, context);
			statusCode = response.getStatusLine().getStatusCode();
			logger.debug("execute url[{}] finish, and status code[{}]",task.getUrl(),statusCode);
			if(HttpUtils.httpResponseStatus(statusCode)){
				return response;
			}else{
				return null;
			}
		}catch (ConnectionPoolTimeoutException e) {
			throw new SocketTimeoutException(e.toString());
		}catch(NotModifiedException e){
			return null;
		}catch (UnknownHostException e) {
			throw new SocketTimeoutException(e.toString());
		}catch (NoHttpResponseException e) {
			throw new SocketTimeoutException(e.toString());
		}catch(SocketTimeoutException e){
			throw e;
		}catch(RuntimeException e){
			logger.error("execute target[{}] unknow exception.",task.getUrl(),e);
			return null;
		}catch(ClientProtocolException e){
			throw new SocketTimeoutException(e.getMessage());
		}catch(IOException e){
			throw new CoreException(e);
		}catch(Exception e){
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 以post方式发送请求
	 * @date 2015年1月9日 下午2:35:04
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private CloseableHttpResponse post(Task task) throws Exception{
		try{
			logger.debug("executing url[{}] start, and post param is[{}].",task.getUrl(),task.getParams());
			Map<String, String> postMap = ConvertUtils.String2Map(task.getParams());
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			for(String key : postMap.keySet()){
				postParams.add(new BasicNameValuePair(key, postMap.get(key)));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
			HttpPost post = new HttpPost(task.getUrl());
			post.setEntity(entity);
			post.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(post, context);
			statusCode = response.getStatusLine().getStatusCode();
			logger.debug("execute url[{}] finish, and status code[{}]",task.getUrl(),statusCode);
			if(HttpUtils.httpResponseStatus(statusCode)){
				return response;
			}else{
				return null;
			}
		}catch (ConnectionPoolTimeoutException e) {
			throw new SocketTimeoutException(e.toString());
		}catch(NotModifiedException e){
			return null;
		}catch (UnknownHostException e) {
			throw new SocketTimeoutException(e.toString());
		}catch (NoHttpResponseException e) {
			throw new SocketTimeoutException(e.toString());
		}catch(SocketTimeoutException e){
			throw e;
		}catch(RuntimeException e){
			logger.error("execute target[{}] unknow exception.",task.getUrl(),e);
			return null;
		}catch(ClientProtocolException e){
			throw new SocketTimeoutException(e.getMessage());
		}catch(IOException e){
			throw new CoreException(e);
		}catch(Exception e){
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 设置cookie请求参数
	 * @date 2014年11月4日 下午3:40:11
	 * @param cookieParam
	 * @return
	 * @throws CoreException
	 */
	private void setCookie(final Cookies cookies) throws CoreException{
		try{
			if(cookies != null ){
				Cookie cookie = new ClientCookie() {
					
					@Override
					public boolean isSecure() {
						return cookies.isSecure();
					}
					
					@Override
					public boolean isPersistent() {
						return false;
					}
					
					@Override
					public boolean isExpired(Date arg0) {
						return false;
					}
					
					@Override
					public int getVersion() {
						return cookies.getVersion();
					}
					
					@Override
					public String getValue() {
						return cookies.getCookieValue();
					}
					
					@Override
					public int[] getPorts() {
						return null;
					}
					
					@Override
					public String getPath() {
						return cookies.getPath();
					}
					
					@Override
					public String getName() {
						return cookies.getCookieName();
					}
					
					@Override
					public Date getExpiryDate() {
						return null;
					}
					
					@Override
					public String getDomain() {
						return cookies.getDomian();
					}
					
					@Override
					public String getCommentURL() {
						return null;
					}
					
					@Override
					public String getComment() {
						return null;
					}
					
					@Override
					public String getAttribute(String arg0) {
						return null;
					}
					
					@Override
					public boolean containsAttribute(String arg0) {
						return false;
					}
				}; 
				cookieStore.addCookie(cookie);
				context.setCookieStore(cookieStore);
			}			
		}catch(Exception e){
			logger.warn("setCookie["+cookies+"] error.",e);
		}
	}
	
	/**
	 * @desc 释放response资源
	 * @date 2015年1月9日 下午2:58:09
	 * @param response
	 * @throws CoreException
	 */
	public void closeResources(CloseableHttpResponse response) throws CoreException{
		try{
			if(response != null){
				response.close();
			}
		}catch(Exception e){
			throw new CoreException("close http resources error."+e);
		}
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	private String getUserAgent() throws CoreException{
		Map<String, String> agents = XmlUtils.getNodes(userAgentXmlPath, "agent",true);
		if(agents == null){
			return "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0";
		}
		int agentCount = agents.size();
		String key = "agent"+new Random().nextInt(agentCount);
		if(!agents.containsKey(key)){
			key = "agent"+new Random(agentCount);
		}
		return agents.get(key);
	}

	public void setUserAgentXmlPath(String userAgentXmlPath) {
		this.userAgentXmlPath = userAgentXmlPath;
	}

	

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
}
