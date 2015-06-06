package org.rency.crawler.service.impl;

import java.util.List;

import org.rency.common.utils.exception.CoreException;
import org.rency.crawler.beans.Cookies;
import org.rency.crawler.repository.CookiesRepository;
import org.rency.crawler.service.CookiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("cookiesService")
public class CookiesServiceImpl implements CookiesService {

	@Autowired
	@Qualifier("cookiesRepository")
	private CookiesRepository cookiesRepository;
	
	@Override
	public List<Cookies> list() throws CoreException {
		return cookiesRepository.list();
	}

	@Override
	public Cookies query(String domian) throws CoreException {
		return cookiesRepository.get(domian);
	}

	@Override
	public boolean add(Cookies cookies) throws CoreException {
		return cookiesRepository.save(cookies);
	}

	@Override
	public boolean update(Cookies cookies) throws CoreException {
		return cookiesRepository.update(cookies);
	}

	@Override
	public boolean delete(String domian) throws CoreException {
		return cookiesRepository.delete(domian);
	}

	@Override
	public boolean deleteAll() throws CoreException {
		return cookiesRepository.deleteAll();
	}

}
