package org.rency.crawler.repository;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Cookies;
import org.springframework.stereotype.Repository;

@Repository("cookiesRepository")
public interface CookiesRepository {

	public List<Cookies> list() throws StoreException;
	
	public Cookies get(String host) throws StoreException;
	
	public Integer save(Cookies cookie) throws StoreException;
	
	public Integer update(Cookies cookie) throws StoreException;
	
	public Integer delete(String host) throws StoreException;
	
	public Integer deleteAll() throws StoreException;
	
}