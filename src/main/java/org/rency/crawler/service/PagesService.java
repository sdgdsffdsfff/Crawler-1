package org.rency.crawler.service;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Pages;

public interface PagesService {
	
	public List<Pages> list() throws StoreException;
	
	public Pages get(String url,String sign) throws StoreException;
	
	public Pages getUnIndexerWithOne() throws StoreException;
	
	public boolean save(Pages page) throws StoreException;
	
	public boolean update(Pages page) throws StoreException;
	
	public boolean delete(String url,String sign) throws StoreException;
	
	public boolean deleteAll() throws StoreException;
	
}