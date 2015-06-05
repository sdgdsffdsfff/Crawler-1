package org.rency.crawler.dao;

import java.util.List;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.springframework.stereotype.Repository;

@Repository("cookiesDao")
public interface CookiesDao {

	public abstract List<Cookies> list() throws CoreException;
	
	public abstract Cookies get(String domian) throws CoreException;
	
	public abstract boolean save(Cookies cookie) throws CoreException;
	
	public abstract boolean update(Cookies cookie) throws CoreException;
	
	public abstract boolean delete(String domian) throws CoreException;
	
	public abstract boolean deleteAll() throws CoreException;
	
}