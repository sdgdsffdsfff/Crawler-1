package org.rency.crawler.service;

import java.util.List;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Pages;

public interface PagesService {
	
	public List<Pages> list() throws CoreException;
	
	public Pages get(String url,String sign) throws CoreException;
	
	public Pages getUnIndexerWithOne() throws CoreException;
	
	public void save(Pages page) throws CoreException;
	
	public void update(Pages page) throws CoreException;
	
	public void delete(String url,String sign) throws CoreException;
	
	public void deleteAll() throws CoreException;
	
}