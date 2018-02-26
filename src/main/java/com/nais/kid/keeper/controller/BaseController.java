package com.nais.kid.keeper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nais.kid.utils.AuthorityContext;
import com.nais.service.UserService;
import com.nais.vo.UserVo;

public abstract class BaseController {
	
	
	@Autowired
	private UserService userService;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected UserVo getUser() {
		try {
			Long userId = AuthorityContext.getUser().getId();
			return userService.getUserById(userId);
		} catch (Exception e) {
			logger.info("get user exception " ,e);
		}
		return null;
	}
	
	/**
	 * 仅获取user
	 * @return
	 */
	protected UserVo getOnlyUser() {
		try {
			Long userId = AuthorityContext.getUser().getId();
			UserVo user = new UserVo();
			user.setId(userId);
			return user;
		} catch (Exception e) {
			logger.info("get user exception " ,e);
		}
		return null;
	}
	
}
