package org.rency.crawler.repository;

import java.util.List;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Pages;
import org.springframework.stereotype.Repository;

@Repository("pagesRepository")
public interface PagesRepository {

	public List<Pages> list() throws CoreException;
	
	public Pages get(Pages pages) throws CoreException;
	
	public Pages getUnIndexerWithOne() throws CoreException;
	
	public void save(Pages page) throws CoreException;
	
	public void update(Pages page) throws CoreException;
	
	public void delete(Pages pages) throws CoreException;
	
	public void deleteAll() throws CoreException;
	
}