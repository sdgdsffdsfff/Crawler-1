package org.rency.crawler.service;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Cookies;

public interface CookiesService {

	public List<Cookies> list() throws StoreException;
	
	public Cookies query(String host) throws StoreException;
	
	public boolean add(Cookies cookies) throws StoreException;
	
	public boolean update(Cookies cookies) throws StoreException;
	
	public boolean delete(String host) throws StoreException;
	
	public boolean deleteAll() throws StoreException;
	
}