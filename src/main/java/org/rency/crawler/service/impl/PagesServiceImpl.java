package org.rency.crawler.service.impl;

import java.util.List;

import org.rency.common.utils.exception.CoreException;
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
	public List<Pages> list() throws CoreException {
		return pagesRepository.list();
	}

	@Override
	public Pages get(String url, String sign) throws CoreException {
		Pages pages = new Pages();
		pages.setUrl(url);
		pages.setSign(sign);
		return pagesRepository.get(pages);
	}

	@Override
	public Pages getUnIndexerWithOne() throws CoreException {
		return pagesRepository.getUnIndexerWithOne();
	}

	@Override
	public void save(Pages page) throws CoreException {
		pagesRepository.save(page);
	}

	@Override
	public void update(Pages page) throws CoreException {
		pagesRepository.update(page);
	}

	@Override
	public void delete(String url, String sign) throws CoreException {
		Pages pages = new Pages();
		pages.setUrl(url);
		pages.setSign(sign);
		pagesRepository.delete(pages);
	}

	@Override
	public void deleteAll() throws CoreException {
		pagesRepository.deleteAll();
	}

}
