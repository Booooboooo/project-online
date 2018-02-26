/**
 * 
 */
/**
 * @author chris
 *
 */
package com.nais.kid.keeper.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nais.kid.keeper.config.WebSecurityConfig;
import com.nais.kid.keeper.domain.AdminGroup;
import com.nais.kid.utils.SessionUtils;
import com.nais.response.GeneralResponse;

@Controller
public class LoginController {
	
	@RequestMapping(value= {"login", "login.html", "/"}, method={RequestMethod.GET})
	public String Login() {
		return "login";
	}
	
	@RequestMapping(value="login", method= {RequestMethod.POST})
	public String login(String account, String password, Model model, HttpSession session) {
		
		if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
			GeneralResponse<Void> resp = GeneralResponse.failure("用户名或者密码不能为空!");
			model.addAttribute("loginResult", "error");
			return "/login";
		}
		String pwd = AdminGroup.getEntry(account);
		if(StringUtils.isEmpty(pwd) || !password.equals(pwd)) {
			model.addAttribute("loginResult", "error");
			return "/login";
		}
		session.setAttribute(WebSecurityConfig.SESSION_KEY, SessionUtils.base64Encoding(account));
		return "index";
	}
}