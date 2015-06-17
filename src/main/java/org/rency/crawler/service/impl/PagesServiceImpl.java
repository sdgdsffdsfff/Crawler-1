package org.rency.crawler.service.impl;

import java.util.List;

import org.rency.common.utils.exception.StoreException;
import org.rency.crawler.beans.Pages;
import org.rency.crawler.repository.PagesRepository;
import org.rency.crawler.service.PagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("pagesService")
public class PagesServiceImpl implements PagesService{
	
	@Autowired
	@Qualifier("pagesRepository")
	private PagesRepository pagesRepository;

	@Override
	public List<Pages> list() throws StoreException {
		return pagesRepository.list();
	}

	@Override
	public Pages get(String url, String sign) throws StoreException {
		Pages pages = new Pages();
		pages.setUrl(url);
		pages.setSign(sign);
		return pagesRepository.get(pages);
	}

	@Override
	public Pages getUnIndexerWithOne() throws StoreException {
		return pagesRepository.getUnIndexerWithOne();
	}

	@Override
	public boolean save(Pages page) throws StoreException {
		Pages p = get(page.getUrl(),page.getSign());
		if(p == null){
			return pagesRepository.save(page) > 0;
		}
		return true;
	}

	@Override
	public boolean update(Pages page) throws StoreException {
		return pagesRepository.update(page) > 0;
	}

	@Override
	public boolean delete(String url, String sign) throws StoreException {
		Pages pages = new Pages();
		pages.setUrl(url);
		pages.setSign(sign);
		return pagesRepository.delete(pages) > 0;
	}

	@Override
	public boolean deleteAll() throws StoreException {
		return pagesRepository.deleteAll() > 0;
	}

}
