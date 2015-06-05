package org.rency.crawler.service.impl;

import java.util.List;

import org.rency.commons.toolbox.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.dao.CookiesDao;
import org.rency.crawler.service.CookiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cookiesService")
public class CookiesServiceImpl implements CookiesService {

	@Autowired
	private CookiesDao cookiesDao;
	
	@Override
	public List<Cookies> list() throws CoreException {
		return cookiesDao.list();
	}

	@Override
	public Cookies query(String domian) throws CoreException {
		return cookiesDao.get(domian);
	}

	@Override
	public boolean add(Cookies cookies) throws CoreException {
		return cookiesDao.save(cookies);
	}

	@Override
	public boolean update(Cookies cookies) throws CoreException {
		return cookiesDao.update(cookies);
	}

	@Override
	public boolean delete(String domian) throws CoreException {
		return cookiesDao.delete(domian);
	}

	@Override
	public boolean deleteAll() throws CoreException {
		return cookiesDao.deleteAll();
	}

}
