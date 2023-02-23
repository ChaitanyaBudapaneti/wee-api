/**
 * 
 */
package com.wee.service;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.wee.entity.Url;
import com.wee.mybatis.mapper.UrlMapper;
import com.wee.repo.UrlRepo;
import com.wee.util.Commons;

/**
 * @author chaitu
 *
 */
@Service
public class UrlServiceImpl implements UrlService{
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UrlClickServiceImpl.class);
	@Autowired
	UrlRepo urlRepo;
	@Autowired UrlMapper urlMapper;
	@Value("${wee.base.url}")
	String weeBaseUrl;
	
	/* (non-Javadoc)
	 * @see com.wee.service.UrlService#findByHash()
	 */
	@Override
	public Optional<Url> findByHash(String hash) {
		return urlRepo.findById(hash);
	}

	/* (non-Javadoc)
	 * @see com.wee.service.UrlService#create(com.wee.entity.Url)
	 */
	@Override
	public String create(Url url) {
		String hash = generateTinyUrl(url);
		if (url.getGenClickId() != null && url.getGenClickId() == true) {
			return weeBaseUrl+ "c/" + hash;
		}
		return weeBaseUrl+hash;
	}
	
	String generateTinyUrl(Url url) {
		String hash = Commons.genHash(url.getOriginalUrl());
		url.setHash(hash);
		try {
			urlRepo.save(url);
			LOGGER.info("Saved tiny url for url: "+url+" successfully");
		}catch (DataIntegrityViolationException e) {
			LOGGER.error("Failed to save tiny url for url: "+url);
			return generateTinyUrl(url);
		}
		return hash;
	}

}
