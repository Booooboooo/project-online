package com.nais.kid.keeper.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nais.constant.ResourceType;
import com.nais.domain.Resource;
import com.nais.kid.keeper.service.GlobalService;
import com.nais.response.GeneralResponse;
import com.nais.service.ResourceService;

@Controller
@RequestMapping("global")
public class GlobalController  extends BaseController{
	
	
	@Autowired
	private GlobalService globalService;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping("/mainpage/list")
	public String entry() {
		return "mainpage/list";
	}
	
	
	@RequestMapping("/mainpage/query.json")
	@ResponseBody
	public List<Resource> queryList(){
		List<Resource> resources = resourceService.getResourceDirect(null, null, null, ResourceType.DIRECT.getIndex());
		return resources;
	}
	
	@RequestMapping(value = "/mainpage/insert.json",method = {RequestMethod.POST})
	@ResponseBody
	public GeneralResponse<String> insert(String fakeName, String pageUrl){
		
		try {
		
			if (StringUtils.isBlank(fakeName)) {
				fakeName = "1.0.0";
			}
			
			logger.info("creating mainpage {}, {}", fakeName, pageUrl);
			
			Long id = globalService.addMainPage(fakeName, pageUrl, getOnlyUser().getId());
			if (id != null) {
				return GeneralResponse.success(String.format("首图%s已经成功创建！", id));
			}

		} catch (Exception e) {
			logger.info("创建首图失败！", e);
		}
		return GeneralResponse.failure("创建首图失败！","创建首图失败！");
		
	}
	
	
}
