package org.rency.crawler.service.impl;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
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
	public List<Cookies> list() throws StoreException {
		return cookiesRepository.list();
	}

	@Override
	public Cookies query(String host) throws StoreException {
		return cookiesRepository.get(host);
	}

	@Override
	public boolean add(Cookies cookies) throws StoreException {
		Cookies c = query(cookies.getHost());
		if(c == null){
			return cookiesRepository.save(cookies) > 0;
		}
		return true;
	}

	@Override
	public boolean update(Cookies cookies) throws StoreException {
		return cookiesRepository.update(cookies) > 0;
	}

	@Override
	public boolean delete(String host) throws StoreException {
		return cookiesRepository.delete(host) > 0;
	}

	@Override
	public boolean deleteAll() throws StoreException {
		return cookiesRepository.deleteAll() > 0;
	}

}
