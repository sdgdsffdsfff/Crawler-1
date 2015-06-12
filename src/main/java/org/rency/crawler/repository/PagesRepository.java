package org.rency.crawler.repository;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Pages;
import org.springframework.stereotype.Repository;

@Repository("pagesRepository")
public interface PagesRepository {

	public List<Pages> list() throws StoreException;
	
	public Pages get(Pages pages) throws StoreException;
	
	public Pages getUnIndexerWithOne() throws StoreException;
	
	public Integer save(Pages page) throws StoreException;
	
	public Integer update(Pages page) throws StoreException;
	
	public Integer delete(Pages pages) throws StoreException;
	
	public Integer deleteAll() throws StoreException;
	
}