package org.rency.crawler.repository;

import java.util.List;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.springframework.stereotype.Repository;

@Repository("cookiesRepository")
public interface CookiesRepository {

	public List<Cookies> list() throws CoreException;
	
	public Cookies get(String domian) throws CoreException;
	
	public boolean save(Cookies cookie) throws CoreException;
	
	public boolean update(Cookies cookie) throws CoreException;
	
	public boolean delete(String domian) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
}