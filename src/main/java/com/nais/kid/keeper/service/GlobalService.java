package com.nais.kid.keeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nais.service.ResourceService;

@Service
public class GlobalService {
	
	@Autowired
	private ResourceService resourceService;
	
	final static String MAIN_FAKE_HEAD =  "MainPage";// AppConfigHelper.getString("app.global.config.main.fake.name", "MainPage");

	public Long addMainPage(String versionDesc, String pageUrl, Long userId) {
		
		return resourceService.addResourceDirect(MAIN_FAKE_HEAD, versionDesc, pageUrl, userId);
	}
	
	
	
}
