/**
 * 
 */
package com.wee.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wee.entity.Url;
import com.wee.service.UrlClickService;
import com.wee.service.UrlService;

/**
 * @author chaitu
 *
 */
@RestController
@RequestMapping("/")
public class UrlController {
	
	@Autowired UrlService urlService;
	@Autowired UrlClickService urlClickService;
	@GetMapping("{hash}")
	void redirect(@PathVariable("hash") String hash, HttpServletResponse httpServletResponse,@RequestHeader("User-Agent") String userAgent) {
		Optional<Url> oUrl = urlService.findByHash(hash);
		urlClickService.save(userAgent, hash);
		oUrl.ifPresent(url->{
		    httpServletResponse.setHeader("Location", url.getOriginalUrl());
		    httpServletResponse.setStatus(302);
		});
		
	}
	
	@GetMapping("details/{hash}")
	Optional<Url> findById(@PathVariable("hash") String hash) {
		return urlService.findByHash(hash);
		
	}
	
	@PostMapping(path= "", consumes = "application/json", produces = "text/plain")
	String create(@RequestBody Url url) {
		return urlService.create(url);
		
	}
	
}