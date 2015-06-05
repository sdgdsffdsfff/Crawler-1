package org.rency.crawler.service;

import java.util.List;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Cookies;

public interface CookiesService {

	public List<Cookies> list() throws CoreException;
	
	public Cookies query(String domian) throws CoreException;
	
	public boolean add(Cookies cookies) throws CoreException;
	
	public boolean update(Cookies cookies) throws CoreException;
	
	public boolean delete(String domian) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
}